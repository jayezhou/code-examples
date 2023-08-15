package com.tpzwl.octopus.netty.chedian.handler;

import java.nio.ByteOrder;

import com.tpzwl.octopus.exeption.StartFlagNotMatchedExeception;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LengthFieldAndStartFlagBasedFrameDecoder extends LengthFieldBasedFrameDecoder {
	// Suppose size of StartFlag is 1 byte
	private byte startFlagOffset;
	private byte startFlagValue;
	
	public LengthFieldAndStartFlagBasedFrameDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset,
			int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast, byte startFlagOffset, byte startFlagValue) {
		super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
		this.startFlagOffset = startFlagOffset;
		this.startFlagValue = startFlagValue;
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		if (log.isDebugEnabled()) {
	        log.debug("ByteBuf: " + ByteBufUtil.hexDump((ByteBuf) in));			
		}

		byte value = in.getByte(startFlagOffset);
		if (startFlagValue != value) {
			in.clear();
			throw new StartFlagNotMatchedExeception("Start flag is not matched");
		}
		return super.decode(ctx, in);
	}

}
