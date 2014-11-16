package com.lefu.remote.netty.blocking;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 简易阻塞读取实现，仅建议在客户端使用！
 * @author jiang.li
 *
 */
public class DefaultBlockingRead implements BlockingRead {
	private final LinkedBlockingQueue<Object> blockingQueue = new LinkedBlockingQueue<Object>();
	private int timeout = Integer.MAX_VALUE;
	
	public DefaultBlockingRead() {
		
	}
	
	public DefaultBlockingRead(int timeout) {
		this.timeout = timeout;
	}
	
	@Override
	public List<Object> read() throws SocketTimeoutException {
		List<Object> l = new ArrayList<Object>();
		if (blockingQueue.size() > 0) {
			int size = blockingQueue.size();
			for (int i = 0; i < size; i++) {
				Object obj = blockingQueue.poll();
				if (obj != null) {
					l.add(obj);
				}
			}
			return l;
		}
		Object obj = null;
		try {
			obj = blockingQueue.poll(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (obj == null) {
			throw new SocketTimeoutException();
		}
		l.add(obj);
		return l;
	}
	
	@Override
	public void clear() {
		this.blockingQueue.clear();
	}
	
	public void put(Object o) {
		try {
			this.blockingQueue.put(o);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
}
