package com.lefu.remote.netty;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lefu.remote.netty.enums.RequestHeadLenType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 针对基础报文格式的解析类
 * 
 * <pre>
 * 2/4 byte(len) + len bytes
 * </pre>
 * 
 * @author jiang.li
 * 
 */
public class DefaultIByteArrayDecoderHandler extends ByteToMessageDecoder {
	public static final int DEFAULT_DATA_MAX_LENGTH = 2048;
	private final Logger log = LoggerFactory.getLogger(getClass());
	private int maxLength = DEFAULT_DATA_MAX_LENGTH;
	private RequestHeadLenType headLenType = RequestHeadLenType.ShortLen;

	public DefaultIByteArrayDecoderHandler() {

	}

	public DefaultIByteArrayDecoderHandler(int maxLength,
			RequestHeadLenType headLenType) {
		this.maxLength = maxLength;
		this.headLenType = headLenType;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		// Will called when connection closed and byte will be zero.
		if (in.readableBytes() < headLenType.getLen()) {
			return;
		}
		in.markReaderIndex();
		int len = 0;
		switch (headLenType) {
		case ByteLen:
			len = in.readByte();
			break;
		case ShortLen:
			len = in.readShort();
			break;
		case IntLen:
			len = in.readInt();
			break;
		default:
			throw new RuntimeException("Unkown RequestHeadLenType " + headLenType.toString());
		}
		if (len > maxLength) {
			in.clear();
			throw new RuntimeException("Data too large " + len);
		}
		if (in.readableBytes() < len) {
			in.resetReaderIndex();
			return;
		}
		in.resetReaderIndex();
		byte[] content = new byte[len + headLenType.getLen()];
		in.readBytes(content);
		out.add(content);
		if (log.isDebugEnabled()) {
			log.debug("Decode data with size {}", len);
		}
	}

	/**
	 * 设置最大的报文长度
	 * 
	 * @param maxLength
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public void setHeadLenType(RequestHeadLenType headLenType) {
		this.headLenType = headLenType;
	}

}
