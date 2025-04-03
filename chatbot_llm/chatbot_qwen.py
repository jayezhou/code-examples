#!/usr/bin/env python3
# Copyright (C) Alibaba Group. All Rights Reserved.
# MIT License (https://opensource.org/licenses/MIT)

import os
import sys
import signal
from http import HTTPStatus
from io import BytesIO

import dashscope
import pyaudio
from dashscope import Generation
from dashscope.audio.tts_v2 import SpeechSynthesizer, ResultCallback
from dashscope.audio.asr import Recognition, RecognitionCallback, RecognitionResult
from pydub import AudioSegment
from pydub.playback import play

sys.path.append(
    os.path.join(os.path.dirname(os.path.abspath(__file__)),
                 '../../../utils/python'))

from RealtimeMp3Player import RealtimeMp3Player

# Global variables for microphone and stream
mic = None
stream = None

# Set recording parameters
sample_rate = 16000  # sampling rate (Hz)
channels = 1  # mono channel
dtype = 'int16'  # data type
format_pcm = 'pcm'  # the format of the audio data
block_size = 3200  # number of frames per buffer

# System prompt for the LLM
system_text = '你是一个闲聊型语音AI助手，主要任务是和用户展开日常性的友善聊天。请不要回复使用任何格式化文本，回复要求口语化，不要使用markdown格式或者列表。'

# Initialize DashScope API key
def init_dashscope_api_key():
    '''
    Set your DashScope API-key. More information:
    https://github.com/aliyun/alibabacloud-bailian-speech-demo/blob/master/PREREQUISITES.md
    '''
    if 'DASHSCOPE_API_KEY' in os.environ:
        dashscope.api_key = os.environ[
            'DASHSCOPE_API_KEY']  # load API-key from environment variable DASHSCOPE_API_KEY
    else:
        dashscope.api_key = 'sk-xxxxxxxx'  # set API-key manually


# Real-time speech recognition callback
class ASRCallback(RecognitionCallback):
    def __init__(self):
        self.recognition = None

    def set_recognition(self, recognition):
        self.recognition = recognition

    def on_open(self) -> None:
        global mic, stream
        print('RecognitionCallback open.')
        mic = pyaudio.PyAudio()
        stream = mic.open(format=pyaudio.paInt16,
                          channels=1,
                          rate=16000,
                          input=True)

    def on_close(self) -> None:
        global mic, stream
        print('RecognitionCallback close.')
        if stream:
            stream.stop_stream()
            stream.close()
        if mic:
            mic.terminate()
        stream = None
        mic = None

    def on_complete(self) -> None:
        print('RecognitionCallback completed.')

    def on_error(self, message) -> None:
        print('RecognitionCallback error: ', message.message)
        if stream and stream.is_active():
            stream.stop_stream()
            stream.close()
        sys.exit(1)

    def on_event(self, result: RecognitionResult) -> None:
        sentence = result.get_sentence()
        if 'text' in sentence:
            print('RecognitionCallback text: ', sentence['text'])
            if RecognitionResult.is_sentence_end(sentence):
                print('RecognitionCallback sentence end.')
                try:
                    self.recognition.stop()
                except Exception:
                    pass

                # Process the recognized text
                query_text = sentence['text']
                synthesize_speech_from_llm_by_streaming_mode(query_text)

                # Restart recognition
                try:
                    self.recognition.start()
                except Exception:
                    pass


# Synthesize speech from LLM by streaming mode
def synthesize_speech_from_llm_by_streaming_mode(query_text: str):
    '''
    Synthesize speech with LLM streaming output text, sync call and playback of MP3 audio streams.
    '''
    player = RealtimeMp3Player()
    # Start player
    player.start()

    # Define a callback to handle the result
    class Callback(ResultCallback):
        def on_open(self):
            pass

        def on_complete(self):
            pass

        def on_error(self, message: str):
            print(f'speech synthesis task failed, {message}')

        def on_close(self):
            pass

        def on_event(self, message):
            pass

        def on_data(self, data: bytes) -> None:
            # Save audio to file
            player.write(data)

    # Call the speech synthesizer callback
    synthesizer_callback = Callback()

    synthesizer = SpeechSynthesizer(
        model='cosyvoice-v1',
        voice='loongstella',
        callback=synthesizer_callback,
    )

    # Prepare for the LLM call
    messages = [{
        'role': 'system',
        'content': system_text
    }, {
        'role': 'user',
        'content': query_text
    }]
    print('>>> query: ' + query_text)
    responses = Generation.call(
        model='qwen-plus',
        messages=messages,
        result_format='message',  # set result format as 'message'
        stream=True,  # enable stream output
        incremental_output=True,  # enable incremental output
    )
    print('>>> answer: ', end='')
    for response in responses:
        if response.status_code == HTTPStatus.OK:
            # Send LLM result to synthesizer
            llm_text_chunk = response.output.choices[0]['message']['content']
            print(llm_text_chunk, end='', flush=True)
            synthesizer.streaming_call(llm_text_chunk)
        else:
            print(
                'Request id: %s, Status code: %s, error code: %s, error message: %s'
                % (
                    response.request_id,
                    response.status_code,
                    response.code,
                    response.message,
                ))
    synthesizer.streaming_complete()
    # Add new line after LLM outputs
    print('')
    print('>>> playback completed')
    print('[Metric] requestId: {}, first package delay ms: {}'.format(
        synthesizer.get_last_request_id(),
        synthesizer.get_first_package_delay()))
    # Stop realtime MP3 player
    player.stop()


# Signal handler for graceful shutdown
def signal_handler(sig, frame):
    print('Ctrl+C pressed, stopping recognition...')
    recognition.stop()
    print('Recognition stopped.')
    sys.exit(0)


# Main function
if __name__ == '__main__':
    init_dashscope_api_key()

    # Create the recognition callback
    callback = ASRCallback()

    recognition = Recognition(
        model='paraformer-realtime-v2',
        format=format_pcm,
        sample_rate=sample_rate,
        semantic_punctuation_enabled=False,
        callback=callback
    )

    callback.set_recognition(recognition)

    # Start recognition
    recognition.start()

    signal.signal(signal.SIGINT, signal_handler)
    print("Press 'Ctrl+C' to stop recording and recognition...")

    while True:
        if stream:
            try:
                data = stream.read(block_size, exception_on_overflow=False)
                recognition.send_audio_frame(data)
            except Exception:
                pass
        else:
            break

    recognition.stop()