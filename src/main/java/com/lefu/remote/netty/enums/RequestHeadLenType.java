package com.lefu.remote.netty.enums;

/**
 * 标识报文长度类型
 * @author jiang.li
 *
 */
public enum RequestHeadLenType {
	/**
	 * 使用Byte作为长度
	 */
	ByteLen(1),
	/**
	 * 使用Short作为长度
	 */
	ShortLen(2),
	/**
	 * 使用Int作为长度
	 */
	IntLen(4);
	
	private RequestHeadLenType(int len) {
		this.len = len;
	}
	
	private int len;

	public int getLen() {
		return len;
	}
}
