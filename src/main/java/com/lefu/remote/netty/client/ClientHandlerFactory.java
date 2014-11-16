package com.lefu.remote.netty.client;

import io.netty.channel.ChannelInboundHandler;

import com.lefu.remote.netty.ChannelHandlerFactoryAdapter;

public class ClientHandlerFactory extends ChannelHandlerFactoryAdapter {

	@Override
	public ChannelInboundHandler newInstance() {
		return new IOClientHandler(this);
	}

}
