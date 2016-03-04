package com.lefu.remote.netty.test.file;

import com.lefu.remote.netty.ChannelHandlerFactory;
import com.lefu.remote.netty.client.IOConnector;

/**
 * 使用数据块的方式发送数据
 * @author jiang.li
 *
 */
public class ChunkedIoConnector extends IOConnector {
	
	public ChunkedIoConnector() {
		
	}
	
	public ChunkedIoConnector(ChannelHandlerFactory handlerFactory) {
		super(handlerFactory);
	}
	
	/**
	 * 替换 ChannelInitializer 的实现
	 * @see io.netty.channel.ChannelInitializer
	 */
	@Override
	public void configure() {
		if (getHandlerFactory() == null) {
			throw new NullPointerException("ChannelHandlerFactory must not be null");
		}
		ChunkedChannelInitializer clientChannelInitializer = new ChunkedChannelInitializer(
				getHandlerFactory());
		clientChannelInitializer.setUseSingleton(isUseSingleton());
		clientChannelInitializer.setBothIdleTime(getBothIdleTime());
		clientChannelInitializer.setEnableIdleHandler(isEnableIdleHandler());
		clientChannelInitializer.setEnableTimeoutHandler(isEnableTimeoutHandler());
		clientChannelInitializer.setReadIdleTime(getReadIdleTime());
		clientChannelInitializer.setTimeout(getTimeout());
		clientChannelInitializer.setWriteIdleTime(getWriteIdleTime());
		setChannelInitializer(clientChannelInitializer);
	}

}
