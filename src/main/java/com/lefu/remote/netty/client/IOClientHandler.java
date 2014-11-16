package com.lefu.remote.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lefu.remote.netty.BlockingReadHandlerAdapter;
import com.lefu.remote.netty.ChannelHandlerFactory;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端句柄的默认实现
 * @author jiang.li
 *
 */
@ChannelHandler.Sharable
public class IOClientHandler extends BlockingReadHandlerAdapter {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public IOClientHandler() {
		
	}
	
	public IOClientHandler(ChannelHandlerFactory channelHandlerFactory) {
		super(channelHandlerFactory);
	}
	
	@Override
	protected void doRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
	}
	
	@Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		log.info("Fired user event {}", evt);
    }
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
		log.error(cause.getMessage(), cause);
		ctx.channel().close().addListener(ChannelFutureListener.CLOSE);
    }
}
