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
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoneBlockExampleServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(NoneBlockExampleServerHandler.class);

	private static final EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(3);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("MyServerHandler 连接已建立...");
		super.channelActive(ctx);
	}

	/**
	 * 读取数据 : 在服务器端读取客户端发送的数据
	 */
	@SuppressWarnings("unused")
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 获取客户端发送过来的消息
		ByteBuf in = (ByteBuf) msg;
		log.info("Server Accept Client Context (" + ctx.channel().remoteAddress() + ")消息 ->"
				+ in.toString(CharsetUtil.UTF_8));

		for (int i = 1; i <= 5; i++) {
			int finalI = i;
			eventExecutorGroup.submit(() -> {
				// 执行耗时操作
				try {
					TimeUnit.SECONDS.sleep(10);
					log.info("异步任务 {}执行，Thread = {}", finalI, Thread.currentThread().getName());
					ctx.writeAndFlush(Unpooled.copiedBuffer("异步任务 " + finalI, CharsetUtil.UTF_8));
					log.info("异步任务 {}执行完毕", finalI);
				} catch (Exception ex) {
					System.out.println("发生异常" + ex.getMessage());
				}
			});
		}
		log.info("channelRead方法执行完毕，Thread = {}", Thread.currentThread().getName());
	}

}
