package com.lefu.remote.netty.client;

import com.lefu.remote.netty.ChannelHandlerFactory;
import com.lefu.remote.netty.AbstractChannelInitializer;
import com.lefu.remote.netty.DefaultChannelInitializer;
import com.lefu.remote.netty.server.IOServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;

/**
 * 示例：默认的客户端实现
 * 
 * <pre>
 * 应用只需要覆盖 {@link IOConnector#configure()}，调整网络配置则覆盖 {@link IOServer#options(ServerBootstrap)}，并提供 {@link ChannelHandlerFactory} 的实现类即可。
 * </pre>
 * 
 * @author jiang.li
 * 
 */
public class IOConnector extends AbstractClientConfigure {
	private ClientOptionsConfigure optionsConfigure;
	private ChannelHandlerFactory handlerFactory;
	private boolean useSingleton = false;
	private boolean enableTimeoutHandler = false;
	private boolean enableIdleHandler = true;
	private int timeout = 90;
	private int readIdleTime = 30;
	private int writeIdleTime = 0;
	private int bothIdleTime = 0;

	public IOConnector() {

	}

	public IOConnector(ChannelHandlerFactory handlerFactory) {
		this.handlerFactory = handlerFactory;
	}

	@Override
	public void configure() {
		if (handlerFactory == null) {
			throw new NullPointerException("ChannelHandlerFactory must not be null");
		}
		DefaultChannelInitializer clientChannelInitializer = new DefaultChannelInitializer(
				handlerFactory);
		clientChannelInitializer.setUseSingleton(useSingleton);
		clientChannelInitializer.setBothIdleTime(bothIdleTime);
		clientChannelInitializer.setEnableIdleHandler(enableIdleHandler);
		clientChannelInitializer.setEnableTimeoutHandler(enableTimeoutHandler);
		clientChannelInitializer.setReadIdleTime(readIdleTime);
		clientChannelInitializer.setTimeout(timeout);
		clientChannelInitializer.setWriteIdleTime(writeIdleTime);
		setChannelInitializer(clientChannelInitializer);
	}

	@Override
	protected void options(Bootstrap bootstrap) {
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
		if (optionsConfigure != null) {
			optionsConfigure.options(bootstrap);
		}
	}

	public void setHandlerFactory(ChannelHandlerFactory handlerFactory) {
		this.handlerFactory = handlerFactory;
	}

	/**
	 * @see {@link DefaultChannelInitializer#setUseSingleton(boolean)}
	 * @param useSingleton
	 */
	public void setUseSingleton(boolean useSingleton) {
		this.useSingleton = useSingleton;
	}

	/**
	 * @see {@link AbstractChannelInitializer#setEnableTimeoutHandler(boolean)}
	 * @param enableTimeoutHandler
	 */
	public void setEnableTimeoutHandler(boolean enableTimeoutHandler) {
		this.enableTimeoutHandler = enableTimeoutHandler;
	}

	/**
	 * @see {@link AbstractChannelInitializer#setEnableIdleHandler(boolean)}
	 * @param enableIdleHandler
	 */
	public void setEnableIdleHandler(boolean enableIdleHandler) {
		this.enableIdleHandler = enableIdleHandler;
	}

	/**
	 * @see {@link AbstractChannelInitializer#setTimeout(int)}
	 * @param timeout
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @see {@link AbstractChannelInitializer#setReadIdleTime(int)}
	 * @param readIdleTime
	 */
	public void setReadIdleTime(int readIdleTime) {
		this.readIdleTime = readIdleTime;
	}

	/**
	 * @see {@link AbstractChannelInitializer#setWriteIdleTime(int)}
	 * @param writeIdleTime
	 */
	public void setWriteIdleTime(int writeIdleTime) {
		this.writeIdleTime = writeIdleTime;
	}

	/**
	 * @see {@link AbstractChannelInitializer#setBothIdleTime(int)}
	 * @param bothIdleTime
	 */
	public void setBothIdleTime(int bothIdleTime) {
		this.bothIdleTime = bothIdleTime;
	}

	public void setOptionsConfigure(ClientOptionsConfigure optionsConfigure) {
		this.optionsConfigure = optionsConfigure;
	}

	public ClientOptionsConfigure getOptionsConfigure() {
		return optionsConfigure;
	}

	public ChannelHandlerFactory getHandlerFactory() {
		return handlerFactory;
	}

	public boolean isUseSingleton() {
		return useSingleton;
	}

	public boolean isEnableTimeoutHandler() {
		return enableTimeoutHandler;
	}

	public boolean isEnableIdleHandler() {
		return enableIdleHandler;
	}

	public int getTimeout() {
		return timeout;
	}

	public int getReadIdleTime() {
		return readIdleTime;
	}

	public int getWriteIdleTime() {
		return writeIdleTime;
	}

	public int getBothIdleTime() {
		return bothIdleTime;
	}

}
