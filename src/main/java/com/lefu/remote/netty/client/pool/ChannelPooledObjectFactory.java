package com.lefu.remote.netty.client.pool;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.lefu.remote.netty.client.NettyClient;

public class ChannelPooledObjectFactory implements PooledObjectFactory<Channel> {
	private final NettyClient nettyClient;
	private final String host;
	private final int port;
	
	public ChannelPooledObjectFactory(NettyClient nettyClient, String host, int port) {
		this.nettyClient = nettyClient;
		this.host = host;
		this.port = port;
	}
	
	@Override
	public PooledObject<Channel> makeObject() throws Exception {
		Channel channel = nettyClient.connect(host, port);
		return new DefaultPooledObject<Channel>(channel);
	}

	@Override
	public void destroyObject(PooledObject<Channel> p) throws Exception {
		p.getObject().close().addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public boolean validateObject(PooledObject<Channel> p) {
		return p.getObject().isActive();
	}

	@Override
	public void activateObject(PooledObject<Channel> p) throws Exception {
		// do nothing
	}

	@Override
	public void passivateObject(PooledObject<Channel> p) throws Exception {
		// do nothing
	}

}
