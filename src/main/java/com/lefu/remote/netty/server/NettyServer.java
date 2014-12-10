package com.lefu.remote.netty.server;

import java.util.Map;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

import com.lefu.remote.netty.NettyConfigure;

/**
 * 服务端行为定义
 * @author jiang.li
 *
 */
public interface NettyServer extends NettyConfigure {
	/**
	 * 监听端口
	 * @param port
	 * @return 返回与端口映射的 {@link Channel} ，与端口映射
	 * @throws Exception
	 */
	public Map<Integer, Channel> bind(int... port) throws Exception;
	
	/**
	 * 获取 Netty 服务端 {@link ServerBootstrap}
	 * @return
	 */
	public ServerBootstrap getBootstrap();
}
