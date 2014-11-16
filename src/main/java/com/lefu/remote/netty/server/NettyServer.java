package com.lefu.remote.netty.server;

import io.netty.bootstrap.ServerBootstrap;

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
	 * @throws Exception
	 */
	public void bind(int... port) throws Exception;
	
	/**
	 * 获取 Netty 服务端 {@link ServerBootstrap}
	 * @return
	 */
	public ServerBootstrap getBootstrap();
}
