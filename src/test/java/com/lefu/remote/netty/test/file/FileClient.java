package com.lefu.remote.netty.test.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.nio.ByteBuffer;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.handler.codec.MessageToByteEncoder;

import com.lefu.remote.netty.ChannelHandlerFactoryAdapter;
import com.lefu.remote.netty.client.IOConnector;
import com.lefu.remote.netty.client.NettyClient;

public class FileClient {
	private NettyClient connector;
	
	public FileClient() {
		connector = new IOConnector(new FileClientHandlerFactory());
		try {
			connector.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Channel getChannel() throws Exception {
		return connector.connect("192.168.14.31", 9999);
	}
	
	/**
	 * 文件传输协议头
	 * <pre>8(FileLength) + N(Stream) </pre>
	 * @param path
	 * @param channel
	 */
	public void sendFile(String path, Channel channel) {
		File f = new File(path);
		System.out.println("File length: " + f.length());
		InputStream in = null;
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA");
			in = new DigestInputStream(new BufferedInputStream(new FileInputStream(f), 10240), digest);
			byte[] temp = new byte[10240];
			int readed = 0;
			boolean isFirst = true;
			while (true) {
				readed = in.read(temp);
				if (readed == -1) {
					break;
				}
				ByteBean byteBean = new ByteBean();
				if (isFirst) {
					isFirst = false;
					ByteBuffer byteBuffer = ByteBuffer.allocate(8 + readed);
					byteBuffer.putLong(f.length());
					byteBuffer.put(temp, 0, readed);
					byteBuffer.flip();
					byteBean.setContent(byteBuffer.array());
					byteBean.setPosition(0);
					byteBean.setLength(byteBuffer.capacity());
				} else {
					byteBean.setContent(temp);
					byteBean.setPosition(0);
					byteBean.setLength(readed);
				}
				ChannelFuture cf = channel.writeAndFlush(byteBean);
				cf.await();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
					System.out.println(HexUtil.bytesToHexString(digest.digest()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void close() {
		this.connector.destroy();
	}
	
	public static void main(String[] args) throws Exception {
		FileClient client = new FileClient();
		Channel channel = client.getChannel();
		client.sendFile("/home/leo/Downloads/VMware-Workstation-Full-10.0.3-1895310.x86_64.bundle", channel);
//		client.sendFile("/home/leo/Downloads/apache-tomcat-8.0.14.tar.gz", channel);
//		client.sendFile("/home/leo/Downloads/mybatis-spring-1.0.1-reference.pdf", channel);
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.close();
	}
	
	public class FileClientHandlerFactory extends ChannelHandlerFactoryAdapter {
		
		@Override
		public ChannelOutboundHandler newEncoder() {
			return new MessageToByteEncoder<ByteBean>() {

				@Override
				protected void encode(ChannelHandlerContext ctx, ByteBean msg,
						ByteBuf out) throws Exception {
					out.writeBytes(msg.getContent(), msg.getPosition(), msg.getLength());
				}
				
			};
		}
		
		@Override
		public ChannelInboundHandler newInstance() {
			return new ChannelInboundHandlerAdapter() {
				
				 @Override
				 public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
					 
				 }
				 
				 @Override
				 public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
					 // Do nothing
				 }
				 
				 @Override
				 public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
				            throws Exception {
					 cause.printStackTrace();
				 }
			};
		}
		
	}
	
}
