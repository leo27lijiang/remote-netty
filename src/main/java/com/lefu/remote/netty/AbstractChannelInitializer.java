package com.lefu.remote.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * 提供了基础的超时和空闲句柄
 * @author jiang.li
 * @see io.netty.handler.timeout.ReadTimeoutHandler
 * @see io.netty.handler.timeout.IdleStateHandler
 */
public abstract class AbstractChannelInitializer extends ChannelInitializer<SocketChannel> {
	private boolean enableTimeoutHandler = false;
	private boolean enableIdleHandler = true;
	protected int timeout = 90;
	protected int readIdleTime = 30;
	protected int writeIdleTime = 0;
	protected int bothIdleTime = 0;
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		if(enableTimeoutHandler) p.addLast(new ReadTimeoutHandler(timeout));
		if(enableIdleHandler) p.addLast(new IdleStateHandler(readIdleTime, writeIdleTime,bothIdleTime));
		doInitChannel(ch);
	}
	
	/**
	 * 在配置业务 Handler 之前进行超时和空闲的 Handler 的注册
	 * @param ch
	 * @throws Exception
	 */
	public abstract void doInitChannel(SocketChannel ch) throws Exception;
	
	/**
	 * 设置超时时间
	 * @param timeout 单位秒
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * 读超时时间，单位秒，小于等于0的值无效
	 * @param readIdleTime
	 */
	public void setReadIdleTime(int readIdleTime) {
		this.readIdleTime = readIdleTime;
	}
	
	/**
	 * 写超时时间，单位秒，小于等于0的值无效
	 * @param writeIdleTime
	 */
	public void setWriteIdleTime(int writeIdleTime) {
		this.writeIdleTime = writeIdleTime;
	}
	
	/**
	 * 共同的超时时间，单位秒，小于等于0的值无效
	 * @param bothIdleTime
	 */
	public void setBothIdleTime(int bothIdleTime) {
		this.bothIdleTime = bothIdleTime;
	}
	
	/**
	 * 是否启用超时 Handler 
	 * <pre>注意：设置超时句柄后，超时触发时会关闭连接</pre>
	 * @param enableTimeoutHandler
	 */
	public void setEnableTimeoutHandler(boolean enableTimeoutHandler) {
		this.enableTimeoutHandler = enableTimeoutHandler;
	}
	
	/**
	 * 是否启用空闲 Handler
	 * @param enableIdleHandler
	 */
	public void setEnableIdleHandler(boolean enableIdleHandler) {
		this.enableIdleHandler = enableIdleHandler;
	}

}
