package com.lefu.remote.netty.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lefu.remote.netty.server.IOServer;
import com.lefu.remote.netty.server.NettyServer;
import com.lefu.remote.netty.server.ServerHandlerFactory;

import junit.framework.TestCase;

public class IOServerTest extends TestCase {
	private NettyServer ioServer;
	
	@Before
	public void setUp() {
		ioServer = new IOServer(new ServerHandlerFactory());
	}
	
	@Test
	public void testServer() {
		try {
			ioServer.bind(8888,9999);
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown() {
		if(ioServer != null) {
			ioServer.destroy();
		}
	}
	
}
