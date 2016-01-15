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
		return connector.connect("192.168.14.9", 9999);
	}
	
	/**
	 * 文件传输协议头
	 * <pre>
	 * HeaderLen  HeaderContent    StreamLen    Stream        SHA
	 * 4(int)   +   N(byte[])   +   8(long)  +  M(byte[])  +  40(String) 
	 * </pre>
	 * @param path
	 * @param param
	 * @param channel
	 */
	public void sendFile(String path, Object param, Channel channel) {
		File f = new File(path);
		System.out.println("File length: " + f.length());
		InputStream in = null;
		MessageDigest digest = null;
		try {
			byte[] header = SerializableUtil.object2Byte(param);
			digest = MessageDigest.getInstance("SHA");
			in = new DigestInputStream(new BufferedInputStream(new FileInputStream(f), 10240), digest);
			byte[] temp = new byte[10240];
			int readed = 0;
			int headerMetaLen = 4 + header.length + 8;// 4(int)   +   N(byte[])   +   8(long)
			ByteBuffer byteBuffer = ByteBuffer.allocate(headerMetaLen); 
			byteBuffer.putInt(header.length);// 4
			byteBuffer.put(header);// N
			byteBuffer.putLong(f.length());// 8
			byteBuffer.flip();
			channel.writeAndFlush(new ByteBean(0, headerMetaLen, byteBuffer.array())).await();// Send header meta data first
			while (true) {
				readed = in.read(temp);
				if (readed == -1) {
					break;
				}
				ByteBean byteBean = new ByteBean();
				byteBean.setContent(temp);
				byteBean.setPosition(0);
				byteBean.setLength(readed);
				ChannelFuture cf = channel.writeAndFlush(byteBean);
				cf.await();
			}
			String sha = HexUtil.bytesToHexString(digest.digest());
			channel.writeAndFlush(new ByteBean(0, sha.length(), sha.getBytes()));
			System.out.println("Client SHA: " + sha);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
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
		client.sendFile("/home/leo/Downloads/bootstrap-3.3.2-dist.zip", new Param("k","v"), channel);
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
