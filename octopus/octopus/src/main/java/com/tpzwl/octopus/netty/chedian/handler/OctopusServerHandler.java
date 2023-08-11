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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * event handler to process receiving messages
 *
 * @author Jibeom Jung akka. Manty
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class OctopusServerHandler extends ChannelInboundHandlerAdapter {
	
    public static final AttributeKey<ByteBuf> REMAINING_BUFFER_ATTRIBUTE_KEY = AttributeKey.newInstance("REMAINING_BUFFER");
    
    public static final int LENGTH_FIELD_OFFSET = 1;	// 报文中长度字段的偏移
    public static final int LENGTH_FIELD_SIZE = 1;		// 报文中长度字段的大小
    public static final int LENGTH_ADJUSTMENT = 1;		// 报文总长度 = 长度字段的偏移 + 长度字段的大小 + 别的字段的大小

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //Assert.notNull(this.channelRepository, "[Assertion failed] - ChannelRepository is required; it must not be null");
    	//ctx.channel().attr(REMAINING_BUFFER_ATTRIBUTE_KEY).set(ctx.alloc().buffer());	// 因为TCP的粘包和拆包，把读入的包先放在这里再进行处理
    	
        ctx.fireChannelActive();
        if (log.isDebugEnabled()) {
            log.debug(ctx.channel().remoteAddress() + "");
        }
        String remoteAddress = ctx.channel().remoteAddress().toString();

        //ctx.writeAndFlush("Your remote address is " + remoteAddress + ".\r\n");
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {    	
    	ByteBuf byteBufRead = (ByteBuf) msg;   	
        //String stringMessage = (String) msg;
         log.info("in: " + ByteBufUtil.hexDump((ByteBuf) msg));
        
        ctx.writeAndFlush(byteBufRead);
    }

    /*
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {    	
    	ByteBuf byteBufRead = (ByteBuf) msg;   	
        //String stringMessage = (String) msg;
        if (log.isDebugEnabled()) {
            log.debug("in: " + ByteBufUtil.hexDump((ByteBuf) msg));
        }
        
    	ByteBuf byteBufRmng = ctx.channel().attr(REMAINING_BUFFER_ATTRIBUTE_KEY).get();
    	byteBufRmng.writeBytes(byteBufRead);
    	
    	while (true) {
	        int readableBytes = byteBufRmng.readableBytes();
	        if (readableBytes < LENGTH_FIELD_OFFSET + LENGTH_FIELD_SIZE) {	// 报文太短读不到长度字段
	        	return;
	        } else {	// 报文够长，可以读到报文中长度字段
	        	int lengthFieldValue = 0;	// 长度字段数值
	        	for (int i = 0; i < LENGTH_FIELD_SIZE; i++) {	// 获取报文长度
	        		lengthFieldValue = lengthFieldValue << 8;		// 左移8位（1个字节）
	        		lengthFieldValue = lengthFieldValue + byteBufRmng.getInt(LENGTH_FIELD_OFFSET + i);
	        	}
	        	
	        	int packLength = lengthFieldValue + LENGTH_FIELD_OFFSET + LENGTH_FIELD_SIZE + LENGTH_ADJUSTMENT;
	        	if (readableBytes < packLength) {  // 小于一个报文长度
	        		return;
	        	} else {
	        		ByteBuf byteBuf = byteBufRmng.readBytes(packLength);
	        		//TODO: 报文处理逻辑
	        	}       		
	        }
    	}
    }
    */
    
//	@Override
//	public void channelReadComplete(final ChannelHandlerContext ctx) {
//		ctx.channel().read();
//		ctx.fireChannelReadComplete();
//	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage(), cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	ByteBuf byteBuf = ctx.channel().attr(REMAINING_BUFFER_ATTRIBUTE_KEY).get();
    	//TODO: 
    	ReferenceCountUtil.release(byteBuf);
    	super.channelInactive(ctx);
    }
    
}
