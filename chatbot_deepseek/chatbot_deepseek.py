# For prerequisites running the following sample, visit https://help.aliyun.com/zh/model-studio/getting-started/first-api-call-to-qwen
import os
import signal  # for keyboard events handling (press "Ctrl+C" to terminate recording)
import sys
from io import BytesIO

import dashscope
import pyaudio
from openai import OpenAI
from dashscope.audio.tts_v2 import *
from dashscope.audio.asr import *
from pydub import AudioSegment
from pydub.playback import play
from datetime import datetime

mic = None
stream = None

# Set recording parameters
sample_rate = 16000  # sampling rate (Hz)
channels = 1  # mono channel
dtype = 'int16'  # data type
format_pcm = 'pcm'  # the format of the audio data
block_size = 3200  # number of frames per buffer

# 设置ASR API Key
dashscope.api_key = 'sk-xxxxxxxx'  # set API-key manually

# 初始化OpenAI客户端
client = OpenAI(
    api_key=dashscope.api_key,
    base_url="https://dashscope.aliyuncs.com/compatible-mode/v1"
)

# Real-time speech recognition callback
class Callback(RecognitionCallback):
    def __init__(self):
        self.recognition = None

    def set_recognition(self, recognition):
        self.recognition = recognition
        
    def on_open(self) -> None:
        global mic
        global stream
        print('RecognitionCallback open.')
        mic = pyaudio.PyAudio()
        stream = mic.open(format=pyaudio.paInt16,
                          channels=1,
                          rate=16000,
                          input=True)

    def on_close(self) -> None:
        global mic
        global stream
        print('RecognitionCallback close.')
        stream.stop_stream()
        stream.close()
        mic.terminate()
        stream = None
        mic = None

    def on_complete(self) -> None:
        print('RecognitionCallback completed.')  # recognition completed

    def on_error(self, message) -> None:
        print('RecognitionCallback task_id: ', message.request_id)
        print('RecognitionCallback error: ', message.message)
        # Stop and close the audio stream if it is running
        if 'stream' in globals() and stream.active:
            stream.stop()
            stream.close()
        # Forcefully exit the program
        sys.exit(1)

    def on_event(self, result: RecognitionResult) -> None:
        sentence = result.get_sentence()
        if 'text' in sentence:
            print('RecognitionCallback text: ', sentence['text'])
            if RecognitionResult.is_sentence_end(sentence):
                print(
                    'RecognitionCallback sentence end, request_id:%s, usage:%s'
                    % (result.get_request_id(), result.get_usage(sentence)))
                
                # # Stop recognition
                try:
                    self.recognition.stop()
                except Exception as e:
                    pass

                print("开始对话：" + datetime.now().strftime("%Y-%m-%d %H:%M:%S"));
                # 调用对话接口，获取对话结果
                completion = client.chat.completions.create(
                    model="deepseek-r1-distill-qwen-14b",  #"deepseek-r1-distill-qwen-14b", # "deepseek-r1-distill-qwen-32b",      #"deepseek-r1",  # 此处以 deepseek-r1 为例，可按需更换模型名称。
                    messages=[
                        {'role': 'user', 'content': sentence['text']}
                    ]
                )

                # 通过reasoning_content字段打印思考过程
                # print("思考过程：")
                # print(completion.choices[0].message.reasoning_content)

                # 通过content字段打印最终答案
                print("获得答案：" + datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
                answer_content = completion.choices[0].message.content
                print(answer_content)

                # 调用合成接口，生成音频数据
                # 初始化合成器
                synthesizer = SpeechSynthesizer(model="cosyvoice-v1", voice="longwan")  
                audio_data = synthesizer.call(answer_content)
                # 使用pydub从内存中加载音频数据
                audio_segment = AudioSegment.from_mp3(BytesIO(audio_data))
                play(audio_segment)

                # # Start recognition
                try:
                    self.recognition.start()
                except Exception as e:
                    pass

def signal_handler(sig, frame):
    print('Ctrl+C pressed, stop recognition ...')
    # Stop recognition
    recognition.stop()
    print('Recognition stopped.')

    # Forcefully exit the program
    sys.exit(0)


# main function
if __name__ == '__main__':
    print('Initializing ...')

    # Create the recognition callback
    callback = Callback()

    recognition = Recognition(
        model='paraformer-realtime-v2',
        format=format_pcm,
        sample_rate=sample_rate,
        semantic_punctuation_enabled=False,
        callback=callback)

    callback.set_recognition(recognition)

    # Start recognition
    recognition.start()

    signal.signal(signal.SIGINT, signal_handler)
    print("Press 'Ctrl+C' to stop recording and recognition...")
    # Create a keyboard listener until "Ctrl+C" is pressed

    # while True:
    #     if stream:
    #         data = stream.read(3200, exception_on_overflow=False)
    #         recognition.send_audio_frame(data)
    #     else:
    #         break

    while True:
        if stream:
            try:
                data = stream.read(3200, exception_on_overflow=False)
                recognition.send_audio_frame(data)
            except Exception as e:
                pass
        else:
            break

    recognition.stop()