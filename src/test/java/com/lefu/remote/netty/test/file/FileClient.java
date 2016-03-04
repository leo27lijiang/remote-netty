package com.lefu.remote.netty.test.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.nio.ByteBuffer;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.concurrent.GenericFutureListener;

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
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 */
	public void sendFile(String path, Object param, final Channel channel) throws NoSuchAlgorithmException, IOException {
		final File f = new File(path);
		System.out.println("File length: " + f.length());
		final MessageDigest digest = MessageDigest.getInstance("SHA");
		final InputStream in = new DigestInputStream(new BufferedInputStream(new FileInputStream(f), 10240), digest);
		final byte[] temp = new byte[10240];
		byte[] header = SerializableUtil.object2Byte(param);
		int headerMetaLen = 4 + header.length + 8;// 4(int)   +   N(byte[])   +   8(long)
		ByteBuffer byteBuffer = ByteBuffer.allocate(headerMetaLen); 
		byteBuffer.putInt(header.length);// 4
		byteBuffer.put(header);// N
		byteBuffer.putLong(f.length());// 8
		byteBuffer.flip();
		channel.writeAndFlush(new ByteBean(0, headerMetaLen, byteBuffer.array())).addListener(new ChunkedFileListener(channel, in, temp, digest));
	}
	
	public void close() {
		this.connector.destroy();
	}
	
	public static void main(String[] args) throws Exception {
		FileClient client = new FileClient();
		final Channel channel = client.getChannel();
		client.sendFile("/home/leo/Downloads/bootstrap-3.3.2-dist.zip", new Param("1","bootstrap-3.3.2-dist.zip"), channel);
		//client.sendFile("/home/leo/Downloads/apache-tomcat-8.0.14.tar.gz", new Param("2","apache-tomcat-8.0.14.tar.gz"), channel);
		//client.sendFile("/home/leo/Downloads/mybatis-spring-1.0.1-reference.pdf", new Param("3","mybatis-spring-1.0.1-reference.pdf"), channel);
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.close();
	}
	
	public class ChunkedFileListener implements GenericFutureListener<ChannelFuture> {
		private final Channel channel;
		private final InputStream in;
		private final byte[] temp;
		private final MessageDigest digest;
		
		public ChunkedFileListener(Channel channel, InputStream in, byte[] temp, MessageDigest digest) {
			this.channel = channel;
			this.in = in;
			this.temp = temp;
			this.digest = digest;
		}
		
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			int readed = in.read(temp);
			if (readed == -1) {
				String sha = HexUtil.bytesToHexString(digest.digest());
				channel.writeAndFlush(new ByteBean(0, sha.length(), sha.getBytes()));
				System.out.println("Client SHA: " + sha);
				in.close();
			} else {
				ByteBean by = new ByteBean();
				by.setContent(temp);
				by.setLength(readed);
				by.setPosition(0);
				channel.writeAndFlush(by).addListener(new ChunkedFileListener(channel, in, temp, digest));
			}
		}
		
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
