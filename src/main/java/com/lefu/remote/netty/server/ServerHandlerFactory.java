package com.lefu.remote.netty.server;

import io.netty.channel.ChannelInboundHandler;

import com.lefu.remote.netty.ChannelHandlerFactoryAdapter;

public class ServerHandlerFactory extends ChannelHandlerFactoryAdapter {

	@Override
	public ChannelInboundHandler newInstance() {
		return new IOServerHandler(this);
	}

}
