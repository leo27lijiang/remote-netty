package com.lefu.remote.netty.client;

import io.netty.bootstrap.Bootstrap;

/**
 * 提供一个便捷的方式调整网络配置
 * @author jiang.li
 *
 */
public interface ClientOptionsConfigure {
	/**
	 * 修改网络配置
	 * @param bootstrap
	 */
	public void options(Bootstrap bootstrap);
	
}
