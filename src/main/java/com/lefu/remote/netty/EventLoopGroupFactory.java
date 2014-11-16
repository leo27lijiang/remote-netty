package com.lefu.remote.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;

/**
 * 线程池的简便工程类
 * @author jiang.li
 *
 */
public class EventLoopGroupFactory {
	private static final Logger log = LoggerFactory.getLogger(EventLoopGroupFactory.class);
	
	/**
	 * 创建 {@link NioEventLoopGroup}
	 * @param size
	 * @return
	 */
	public static NioEventLoopGroup newNioLoopGroup(int size) {
		NioEventLoopGroup group = null;
		if (size <= 0) {
			group = new NioEventLoopGroup();
		} else {
			group = new NioEventLoopGroup(size);
		}
		log.info("Create new NioEventLoopGroup with {} threads", group.executorCount());
		return group;
	}
	
	/**
	 * 创建 {@link OioEventLoopGroup}
	 * @param maxChannel 线程池能处理的最大 {@link Channel} 数量
	 * @return
	 */
	public static OioEventLoopGroup newOioLoopGroup(int maxChannel) {
		OioEventLoopGroup group = null;
		if (maxChannel <= 0) {
			maxChannel = 1;
			group = new OioEventLoopGroup(maxChannel);
		} else {
			group = new OioEventLoopGroup(maxChannel);
		}
		log.info("Create new OioEventLoopGroup with {} max channels", maxChannel);
		return group;
	}
	
}
