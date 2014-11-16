package com.lefu.remote.netty;

import com.lefu.remote.netty.enums.RequestHeadLenType;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;

public abstract class ChannelHandlerFactoryAdapter implements
		ChannelHandlerFactory {
	protected int maxDataLength = 2048;
	protected boolean blockingRead = false;
	protected int timeout = Integer.MAX_VALUE;
	protected RequestHeadLenType headLenType = RequestHeadLenType.ShortLen;
	
	@Override
	public abstract ChannelInboundHandler newInstance();

	@Override
	public ChannelInboundHandler newDecoder() {
		return new DefaultIByteArrayDecoderHandler(maxDataLength, headLenType);
	}

	@Override
	public ChannelOutboundHandler newEncoder() {
		return new DefaultByteArrayEncoderHandler();
	}

	/**
	 * @see {@link DefaultIByteArrayDecoderHandler#setMaxLength(int)}
	 * @param maxDataLength
	 */
	public void setMaxDataLength(int maxDataLength) {
		this.maxDataLength = maxDataLength;
	}

	public boolean isBlockingRead() {
		return blockingRead;
	}

	public int getTimeout() {
		return this.timeout;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setBlockingRead(boolean blockingRead) {
		this.blockingRead = blockingRead;
	}

	public void setHeadLenType(RequestHeadLenType headLenType) {
		this.headLenType = headLenType;
	}

}