package com.lefu.remote.netty.blocking;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketTimeoutException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 简易阻塞读取实现，仅建议在客户端使用！
 * @author jiang.li
 *
 */
public class DefaultBlockingRead implements BlockingRead {
	private final LinkedBlockingQueue<Object> blockingQueue = new LinkedBlockingQueue<Object>();
	private ChannelHandlerContext channelHandlerContext;
	private int timeout = Integer.MAX_VALUE;
	
	public DefaultBlockingRead() {
		
	}
	
	public DefaultBlockingRead(ChannelHandlerContext ctx, int timeout) {
		this.channelHandlerContext = ctx;
		this.timeout = timeout;
	}
	
	@Override
	public Object read() throws SocketTimeoutException {
		Object obj = null;
		try {
			obj = blockingQueue.poll(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (obj == null) {
			channelHandlerContext.close().addListener(ChannelFutureListener.CLOSE);
			throw new SocketTimeoutException();
		}
		return obj;
	}
	
	@Override
	public void clear() {
		this.blockingQueue.clear();
	}
	
	public void put(Object o) {
		try {
			this.blockingQueue.put(o);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
		this.channelHandlerContext = channelHandlerContext;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
}
