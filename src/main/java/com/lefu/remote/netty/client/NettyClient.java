package com.lefu.remote.netty.client;

import java.net.SocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;

import com.lefu.remote.netty.NettyConfigure;

/**
 * 客户端行为定义
 * @author jiang.li
 *
 */
public interface NettyClient extends NettyConfigure {
	/**
	 * 建立新的连接
	 * @param host
	 * @param port
	 * @return
	 * @throws Exception
	 */
	public Channel connect(String host, int port) throws Exception;
	/**
	 * 
	 * @param socketAddress
	 * @return
	 * @throws Exception
	 */
	public Channel connect(SocketAddress socketAddress) throws Exception;
	
	/**
	 * 获取Netty {@link Bootstrap}
	 * @return
	 */
	public Bootstrap getBootstrap();
}
