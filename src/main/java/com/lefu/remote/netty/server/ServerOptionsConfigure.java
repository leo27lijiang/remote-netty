package com.lefu.remote.netty.server;

import io.netty.bootstrap.ServerBootstrap;

/**
 * 提供一个便捷的方式调整网络配置
 * @author jiang.li
 *
 */
public interface ServerOptionsConfigure {
	/**
	 * 修改网络配置
	 * @param bootstrap
	 */
	public void options(ServerBootstrap bootstrap);
	
}
