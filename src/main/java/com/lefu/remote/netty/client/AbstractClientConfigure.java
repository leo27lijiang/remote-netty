package com.lefu.remote.netty.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lefu.remote.netty.EventLoopGroupFactory;

/**
 * 
 * @author jiang.li
 *
 */
public abstract class AbstractClientConfigure implements NettyClient {
	private final Logger log = LoggerFactory.getLogger(getClass());
	protected Bootstrap bootstrap;
	protected NioEventLoopGroup bossGroup;
	protected ChannelInitializer<? extends Channel> channelInitializer;
	
	private boolean bossGroupSetted = false;
	private AtomicBoolean isStarted = new AtomicBoolean(false);
	private AtomicBoolean isShutdown = new AtomicBoolean(false);
	private int bossGroupSize = 1;
	
	/**
	 * 配置Client options
	 * @param bootstrap
	 */
	protected abstract void options(Bootstrap bootstrap);
	
	@Override
	public Channel connect(String host, int port) throws Exception {
		SocketAddress socketAddress = new InetSocketAddress(host, port);
		return connect(socketAddress);
	}
	
	@Override
	public Channel connect(SocketAddress socketAddress) throws Exception {
		if (!isStarted.get()) {
			throw new IllegalStateException("Netty client bootstrap is not start!");
		}
		ChannelFuture channelFuture = bootstrap.connect(socketAddress);
		final CountDownLatch downLatch = new CountDownLatch(1);
		channelFuture.addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture future)
					throws Exception {
				downLatch.countDown();
			}});
		downLatch.await();
		if (!channelFuture.isSuccess()) {
			throw new RuntimeException("Connect to " + socketAddress.toString() + " fail");
		}
		return channelFuture.channel();
	}
	
	@Override
	public void init() throws Exception {
		if (isStarted.get()) {
			throw new IllegalStateException("Netty client bootstrap already started!");
		}
		configure();
		if (channelInitializer == null) {
			throw new IllegalStateException("ChannelInitializer is null, unset in configure() ?");
		}
		if (!bossGroupSetted) {
			bossGroup = EventLoopGroupFactory.newNioLoopGroup(bossGroupSize);
		}
		bootstrap = new Bootstrap();
		bootstrap.group(bossGroup)
				.channel(NioSocketChannel.class)
				.handler(channelInitializer);
		options(bootstrap);
		isStarted.set(true);
		isShutdown.set(false);
		log.info(String.format("Netty client started with properties -> [BossGroupSize=%1$d]", bossGroup.executorCount()));
	}
	
	@Override
	public void destroy() {
		if (isShutdown.get()) {
			return;
		}
		if (bossGroup != null) {
			bossGroup.shutdownGracefully();
		}
		isStarted.set(false);
		isShutdown.set(true);
		bossGroupSetted = false;
		log.info("Netty client bootstrap is going shutdown!");
	}
	
	public NioEventLoopGroup getBossGroup() {
		return bossGroup;
	}

	public void setBossGroup(NioEventLoopGroup bossGroup) {
		if (bossGroup != null) {
			bossGroupSetted = true;
			this.bossGroup = bossGroup;
		}
	}

	public void setBossGroupSize(int bossGroupSize) {
		this.bossGroupSize = bossGroupSize;
	}

	protected void setChannelInitializer(
			ChannelInitializer<? extends Channel> channelInitializer) {
		this.channelInitializer = channelInitializer;
	}

	public boolean getIsStarted() {
		return isStarted.get();
	}

	public boolean getIsShutdown() {
		return isShutdown.get();
	}

	public Bootstrap getBootstrap() {
		return bootstrap;
	}
	
}
