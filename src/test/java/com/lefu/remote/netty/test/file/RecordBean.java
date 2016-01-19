package com.lefu.remote.netty.test.file;

import java.io.OutputStream;
import java.security.MessageDigest;

public class RecordBean {
	private volatile boolean started = false;
	private volatile boolean streamFinished = false;
	private volatile long offset = 0l;
	private long total = 0l;
	private OutputStream out;
	private String file;
	private MessageDigest digest;
	
	public boolean isStarted() {
		return started;
	}
	public void setStarted(boolean started) {
		this.started = started;
	}
	public boolean isStreamFinished() {
		return streamFinished;
	}
	public void setStreamFinished(boolean streamFinished) {
		this.streamFinished = streamFinished;
	}
	public long getOffset() {
		return offset;
	}
	public void setOffset(long offset) {
		this.offset = offset;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public OutputStream getOut() {
		return out;
	}
	public void setOut(OutputStream out) {
		this.out = out;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public MessageDigest getDigest() {
		return digest;
	}
	public void setDigest(MessageDigest digest) {
		this.digest = digest;
	}
	
}
