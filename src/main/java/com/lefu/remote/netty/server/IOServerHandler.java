package com.lefu.remote.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lefu.remote.netty.BlockingReadHandlerAdapter;
import com.lefu.remote.netty.ChannelHandlerFactory;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

/**
 * 服务端句柄的默认实现
 * @author jiang.li
 *
 */
@ChannelHandler.Sharable
public class IOServerHandler extends BlockingReadHandlerAdapter {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public IOServerHandler() {
		
	}
	
	public IOServerHandler(ChannelHandlerFactory channelHandlerFactory) {
		super(channelHandlerFactory);
	}
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		SocketChannel channel = (SocketChannel) ctx.channel();
		log.info(String.format("New connection from %1$s:%2$d", channel.remoteAddress().getHostString(), channel.remoteAddress().getPort()));
    }
	
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		SocketChannel channel = (SocketChannel) ctx.channel();
		log.info(String.format("Connection from %1$s:%2$d closed", channel.remoteAddress().getHostString(), channel.remoteAddress().getPort()));
    }
	
	@Override
	protected void doRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		byte[] content = (byte[]) msg;
		boolean wrote = false;
		while (!wrote) {
			while (ctx.channel().isWritable()) {
				ctx.writeAndFlush(content);
				wrote = true;
				break;
			}
		}
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
