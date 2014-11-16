package com.lefu.remote.netty.test.client;

import java.nio.ByteBuffer;

import io.netty.channel.Channel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lefu.remote.netty.client.ClientHandlerFactory;
import com.lefu.remote.netty.client.IOConnector;
import com.lefu.remote.netty.client.NettyClient;

import junit.framework.TestCase;

public class IOClientTest extends TestCase {
	private NettyClient connector;
	
	@Before
	public void setUp() {
		connector = new IOConnector(new ClientHandlerFactory());
		try {
			connector.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testConnector() {
		try {
			Channel channel = connector.connect("192.168.13.64", 9999);
			ByteBuffer buffer = ByteBuffer.allocate(10);
			buffer.putShort((short)8);
			buffer.putLong(3l);
			buffer.flip();
			boolean writedMsg = false;
			while(!writedMsg && channel.isWritable()) {
				channel.writeAndFlush(buffer.array());
				writedMsg = true;
				System.out.println("Write message finished.");
			}
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown() {
		connector.destroy();
	}
	
}
