package com.lefu.remote.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lefu.remote.netty.blocking.BlockingRead;
import com.lefu.remote.netty.blocking.DefaultBlockingRead;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;

/**
 * 为阻塞读提供的业务句柄适配类
 * @author jiang.li
 *
 */
public abstract class BlockingReadHandlerAdapter extends ChannelInboundHandlerAdapter {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private ChannelHandlerFactory channelHandlerFactory;
	
	public BlockingReadHandlerAdapter() {
		
	}
	
	public BlockingReadHandlerAdapter(ChannelHandlerFactory channelHandlerFactory) {
		this.channelHandlerFactory = channelHandlerFactory;
	}
	
	@Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		if (channelHandlerFactory == null) {
			throw new NullPointerException("ChannelHandlerFactory is null, you show set it in your InboundHandler");
		}
		if (channelHandlerFactory.isBlockingRead()) {
			Attribute<BlockingRead> r = ctx.channel().attr(BlockingRead.CHANNEL_BLOCKING_READ_KEY);
			r.set(new DefaultBlockingRead(ctx, channelHandlerFactory.getTimeout()));
			log.warn("Blocking read is enabled, do not use it in server handler.");
		}
	}
	
	@Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		if (channelHandlerFactory.isBlockingRead()) {
			Attribute<BlockingRead> r = ctx.channel().attr(BlockingRead.CHANNEL_BLOCKING_READ_KEY);
			r.get().clear();
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (channelHandlerFactory.isBlockingRead()) {
			Attribute<BlockingRead> r = ctx.channel().attr(BlockingRead.CHANNEL_BLOCKING_READ_KEY);
			if (r.get() == null) {
				throw new NullPointerException("ChannelHandlerFactory is not allow blocking read or channelRegistered was overrided ?");
			}
			((DefaultBlockingRead)r.get()).put(msg);
		}
		doRead(ctx, msg);
	}
	
	protected abstract void doRead(ChannelHandlerContext ctx, Object msg) throws Exception;

	public ChannelHandlerFactory getChannelHandlerFactory() {
		return channelHandlerFactory;
	}

	public void setChannelHandlerFactory(ChannelHandlerFactory channelHandlerFactory) {
		this.channelHandlerFactory = channelHandlerFactory;
	}
}
