package com.lefu.remote.netty.client.pool;

import io.netty.channel.Channel;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lefu.remote.netty.client.NettyClient;

/**
 * 默认的连接工厂实现
 * @author jiang.li
 *
 */
public class DefaultConnectionPoolFactory implements ConnectionPoolFactory {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final NettyClient nettyClient;
	private GenericObjectPoolConfig defaultPoolConfig = new GenericObjectPoolConfig();
	
	public DefaultConnectionPoolFactory(NettyClient nettyClient) {
		this.nettyClient = nettyClient;
	}
	
	@Override
	public GenericObjectPool<Channel> buildPool(String host, int port) {
		return buildPool(host, port, null);
	}
	
	@Override
	public GenericObjectPool<Channel> buildPool(String host, int port, GenericObjectPoolConfig config) {
		if (config == null) {
			config = defaultPoolConfig;
		}
		GenericObjectPool<Channel> pool = new GenericObjectPool<Channel>(new ChannelPooledObjectFactory(nettyClient, host, port), config);
		log.info(String.format("Create connection pool with properties -> [Host=%1$s,Port=%2$d]", host, port));
		return pool;
	}
}
