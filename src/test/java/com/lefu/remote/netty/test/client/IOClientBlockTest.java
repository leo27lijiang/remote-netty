package com.lefu.remote.netty.test.client;

import java.nio.ByteBuffer;

import io.netty.channel.Channel;
import io.netty.util.Attribute;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lefu.remote.netty.blocking.BlockingRead;
import com.lefu.remote.netty.client.ClientHandlerFactory;
import com.lefu.remote.netty.client.IOConnector;
import com.lefu.remote.netty.client.NettyClient;

import junit.framework.TestCase;

public class IOClientBlockTest extends TestCase {
	private NettyClient client;
	private ClientHandlerFactory handlerFactory = new ClientHandlerFactory();
	
	@Before
	public void setUp() {
		handlerFactory.setBlockingRead(true);
		handlerFactory.setTimeout(5000);
		client = new IOConnector(handlerFactory);
		try {
			client.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBlockingRead() throws Exception{
		Channel channel = client.connect("192.168.13.64", 8888);
		ByteBuffer buffer = ByteBuffer.allocate(10);
		buffer.putShort((short)8);
		buffer.putLong(3l);
		buffer.flip();
		boolean wrote = false;
		while (!wrote) {
			while (channel.isWritable()) {
				channel.writeAndFlush(buffer.array());
				wrote = true;
				break;
			}
		}
		Attribute<BlockingRead> value = channel.attr(BlockingRead.CHANNEL_BLOCKING_READ_KEY);
		if (value.get() == null) {
			throw new NullPointerException();
		}
		Object result = value.get().read();
		System.out.println(result);
		System.in.read();
	}
	
	@After
	public void tearDown() {
		client.destroy();
	}
	
}
