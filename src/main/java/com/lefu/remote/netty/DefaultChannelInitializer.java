package com.lefu.remote.netty;


import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * {@link ChannelInitializer} 的默认实现
 * @author jiang.li
 *
 */
public class DefaultChannelInitializer extends
		AbstractChannelInitializer {
	private ChannelHandlerFactory handlerFactory;
	private ChannelInboundHandler ioHandler;
	private ChannelOutboundHandler encodeHandler;
	private boolean useSingleton = true;
	
	public DefaultChannelInitializer() {
		
	}
	
	public DefaultChannelInitializer(ChannelHandlerFactory handlerFactory) {
		this.handlerFactory = handlerFactory;
	}
	
	@Override
	public void doInitChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		if (useSingleton) {
			if (ioHandler == null) {
				ioHandler = handlerFactory.newInstance();
			}
			if (encodeHandler == null) {
				encodeHandler = handlerFactory.newEncoder();
			}
			p.addLast(handlerFactory.newDecoder(), encodeHandler, ioHandler);
		} else {
			p.addLast(handlerFactory.newDecoder(), handlerFactory.newEncoder(), handlerFactory.newInstance());
		}
	}
	
	/**
	 * 所有句柄是否使用单例模式，单例模式有助与减少GC，但是 DecoderHandler 的默认实现是不支持单例模式的
	 * @param useSingleton
	 */
	public void setUseSingleton(boolean useSingleton) {
		this.useSingleton = useSingleton;
	}

	public void setHandlerFactory(ChannelHandlerFactory handlerFactory) {
		this.handlerFactory = handlerFactory;
	}

}
