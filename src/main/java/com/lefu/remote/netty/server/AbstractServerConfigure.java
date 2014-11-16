package com.lefu.remote.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lefu.remote.netty.EventLoopGroupFactory;

/**
 * {@link ServerBootstrap} 的基础配置类
 * <pre>
 * 任务类继承此类即可方便的实现Netty的服务端。
 * </pre>
 * @author jiang.li
 *
 */
public abstract class AbstractServerConfigure implements NettyServer {
	private final Logger log = LoggerFactory.getLogger(getClass());
	protected ServerBootstrap bootstrap;
	protected NioEventLoopGroup bossGroup;
	protected NioEventLoopGroup workGroup;
	protected ChannelInitializer<? extends Channel> channelInitializer;
	protected Map<Integer, Channel> serverChannels = new Hashtable<Integer, Channel>();
	protected LogLevel nettyLogLevel = LogLevel.INFO;
	
	private AtomicBoolean isStarted = new AtomicBoolean(false);
	private AtomicBoolean isShutdown = new AtomicBoolean(false);
	private int bossGroupSize = Runtime.getRuntime().availableProcessors();
	private int workGroupSize = Runtime.getRuntime().availableProcessors() * 4;
	private boolean bossGroupSetted = false;
	private boolean workGroupSetted = false;
	
	/**
	 * 配置 Parent options 和 Child options
	 * @param bootstrap
	 */
	protected abstract void options(ServerBootstrap bootstrap);
	
	@Override
	public void init() throws Exception {
		if (isStarted.get()) {
			throw new IllegalStateException("Netty server bootstrap already started!");
		}
		configure();
		if (channelInitializer == null) {
			throw new IllegalStateException("ChannelInitializer is null, unset in configure() ?");
		}
		if (!bossGroupSetted) {
			bossGroup = EventLoopGroupFactory.newNioLoopGroup(bossGroupSize);
		}
		if (!workGroupSetted) {
			workGroup = EventLoopGroupFactory.newNioLoopGroup(workGroupSize);
		}
		bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(nettyLogLevel))
				.childHandler(channelInitializer);
		options(bootstrap);
		isStarted.set(true);
		isShutdown.set(false);
	}
	
	@Override
	public void bind(int... ports) throws Exception {
		if (ports == null) {
			throw new NullPointerException();
		}
		init();
		for (int port : ports) {
			ChannelFuture channelFuture = bootstrap.bind(port);
			Channel serverChannel = channelFuture.sync().channel();
			serverChannels.put(new Integer(port), serverChannel);
		}
		log.info(String.format("Netty server started, listen on %1$s with properties -> [BossGroupSize=%2$d,WorkGroupSize=%3$d]", 
				Arrays.toString(ports), bossGroup.executorCount(), workGroup.executorCount()));
	}
	
	@Override
	public void destroy() {
		if (isShutdown.get()) {
			return;
		}
		if (bossGroup != null) {
			bossGroup.shutdownGracefully();
		}
		if (workGroup != null) {
			workGroup.shutdownGracefully();
		}
		isShutdown.set(true);
		isStarted.set(false);
		bossGroupSetted = false;
		workGroupSetted = false;
		serverChannels.clear();
		log.info("Netty server bootstrap is going shutdown!");
	}
	
	public NioEventLoopGroup getBossGroup() {
		return bossGroup;
	}

	public NioEventLoopGroup getWorkGroup() {
		return workGroup;
	}

	public void setBothGroupSize(int bossGroupSize, int workGroupSize) {
		setBossGroupSize(bossGroupSize);
		setWorkGroupSize(workGroupSize);
	}
	
	public void setBossGroupSize(int bossGroupSize) {
		if(bossGroupSize <= 0) {
			throw new IllegalArgumentException();
		}
		this.bossGroupSize = bossGroupSize;
	}

	public void setWorkGroupSize(int workGroupSize) {
		if (workGroupSize <= 0) {
			throw new IllegalArgumentException();
		}
		this.workGroupSize = workGroupSize;
	}

	public void setBossGroup(NioEventLoopGroup bossGroup) {
		if (bossGroup != null) {
			this.bossGroup = bossGroup;
			this.bossGroupSetted = true;
		}
	}

	public void setWorkGroup(NioEventLoopGroup workGroup) {
		if (workGroup != null) {
			this.workGroup = workGroup;
			this.workGroupSetted = true;
		}
	}

	protected void setChannelInitializer(
			ChannelInitializer<? extends Channel> channelInitializer) {
		this.channelInitializer = channelInitializer;
	}

	public void setNettyLogLevel(LogLevel nettyLogLevel) {
		this.nettyLogLevel = nettyLogLevel;
	}

	public boolean getIsStarted() {
		return isStarted.get();
	}

	public boolean getIsShutdown() {
		return isShutdown.get();
	}

	public ServerBootstrap getBootstrap() {
		return bootstrap;
	}
	
}
