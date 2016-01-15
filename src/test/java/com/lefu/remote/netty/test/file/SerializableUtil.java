package com.lefu.remote.netty.test.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化工具类，默认使用JDK的实现
 * @author jiang.li
 *
 */
public class SerializableUtil {
	public static final int TEMP_SIZE = 1024;
	
	public static byte[] object2Byte(Object obj) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream(TEMP_SIZE);
		ObjectOutputStream outStream = new ObjectOutputStream(stream);
		outStream.writeObject(obj);
		outStream.flush();
		return stream.toByteArray();
	}
	
	public static Object byte2Object(byte[] c) throws IOException, ClassNotFoundException {
		ByteArrayInputStream stream = new ByteArrayInputStream(c);
		ObjectInputStream inStream = new ObjectInputStream(stream);
		return inStream.readObject();
	}
	
}
