package com.lefu.remote.netty;

import com.lefu.remote.netty.blocking.BlockingRead;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;

/**
 * 业务入口句柄工厂类
 * @author jiang.li
 *
 */
public interface ChannelHandlerFactory {
	/**
	 * 指示当前的 {@link ChannelInboundHandler} 是否开启阻塞读取模式
	 * @see {@link BlockingRead#read()}
	 * @return
	 */
	public boolean isBlockingRead();
	/**
	 * 阻塞读取的超时时间，单位毫秒
	 * @return
	 */
	public int getTimeout();
	/**
	 * 创建新业务句柄实例
	 * @return
	 */
	public ChannelInboundHandler newInstance();
	/**
	 * 创建解码器句柄
	 * <pre>
	 * 继承 {@link ByteToMessageDecoder} 的解码器不允许共享，所以在 {@link ChannelPipeline} 初始化时不是单例模式。
	 * </pre>
	 * @return
	 */
	public ChannelInboundHandler newDecoder();
	/**
	 * 创建编码器句柄
	 * @return
	 */
	public ChannelOutboundHandler newEncoder();
}
