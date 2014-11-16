package com.lefu.remote.netty.test.client;

import java.nio.ByteBuffer;

import io.netty.channel.Channel;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lefu.remote.netty.client.ClientHandlerFactory;
import com.lefu.remote.netty.client.IOConnector;
import com.lefu.remote.netty.client.NettyClient;
import com.lefu.remote.netty.client.pool.ConnectionPoolFactory;
import com.lefu.remote.netty.client.pool.DefaultConnectionPoolFactory;

import junit.framework.TestCase;

public class PooledClientTest extends TestCase {
	private NettyClient nettyClient;
	private ConnectionPoolFactory connectionPoolFactory;
	private GenericObjectPool<Channel> pool;
	
	@Before
	public void setUp() {
		nettyClient = new IOConnector(new ClientHandlerFactory());
		try {
			nettyClient.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		connectionPoolFactory = new DefaultConnectionPoolFactory(nettyClient);
		pool = connectionPoolFactory.buildPool("192.168.13.64", 8888);
	}
	
	@Test
	public void testPool() {
		Channel channel = null;
		try {
			channel = pool.borrowObject();
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
			if (channel != null) {
				try {
					pool.invalidateObject(channel);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				channel = null;
			}
		} finally {
			if (channel != null) {
				pool.returnObject(channel);
			}
		}
	}
	
	@After
	public void tearDown() {
		pool.close();
		nettyClient.destroy();
	}
	
}
