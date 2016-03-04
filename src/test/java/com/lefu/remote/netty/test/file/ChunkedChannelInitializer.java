package com.lefu.remote.netty.test.file;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

import com.lefu.remote.netty.ChannelHandlerFactory;
import com.lefu.remote.netty.DefaultChannelInitializer;

public class ChunkedChannelInitializer extends DefaultChannelInitializer {
	
	public ChunkedChannelInitializer() {
		
	}
	
	public ChunkedChannelInitializer(ChannelHandlerFactory handlerFactory) {
		super(handlerFactory);
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
			p.addLast(handlerFactory.newDecoder(), new ChunkedWriteHandler(), encodeHandler, ioHandler);
		} else {
			p.addLast(handlerFactory.newDecoder(), new ChunkedWriteHandler(), handlerFactory.newEncoder(), handlerFactory.newInstance());
		}
	}
}
