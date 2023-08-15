/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tpzwl.octopus.netty.chedian.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
//import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.RequiredArgsConstructor;

import java.nio.ByteOrder;

import org.springframework.stereotype.Component;

/**
 * Channel Initializer
 *
 * @author Jibeom Jung akka. Manty
 */
@Component
@RequiredArgsConstructor
public class OctopusChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final OctopusServerHandler octopusServerHandler;
    //private final StringEncoder stringEncoder = new StringEncoder();
    //private final StringDecoder stringDecoder = new StringDecoder();

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        // Add the text line codec combination first,
        //pipeline.addLast(new LengthFieldBasedFrameDecoder(0xffff, 1, 1, 1, 0));
        pipeline.addLast(new LengthFieldAndStartFlagBasedFrameDecoder(ByteOrder.BIG_ENDIAN, 0xffff, 1, 1, 1, 0, true, (byte)0x00, (byte)0x68));

        //pipeline.addLast(stringDecoder);
        //pipeline.addLast(stringEncoder);
        pipeline.addLast(octopusServerHandler);
    }
}
