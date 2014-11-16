package com.lefu.remote.netty;

import io.netty.channel.ChannelInitializer;

/**
 * Netty 配置定义
 * @author jiang.li
 *
 */
public interface NettyConfigure {
	/**
	 * 用于配置 {@link ChannelInitializer}
	 */
	public void configure();
	/**
	 * 启动服务
	 * @throws Exception
	 */
	public void init() throws Exception;
	/**
	 * 关闭服务
	 */
	public void destroy();
}
