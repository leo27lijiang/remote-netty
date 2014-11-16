package com.lefu.remote.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 针对 byte[] 的编码器，在 {@link ChannelPipeline} 中可以定义为共享类。
 * @author jiang.li
 *
 */
@ChannelHandler.Sharable
public class DefaultByteArrayEncoderHandler extends MessageToByteEncoder<byte[]> {
	
	public DefaultByteArrayEncoderHandler() {
		super(false);
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out)
			throws Exception {
		out.writeBytes(msg);
	}
	
}
