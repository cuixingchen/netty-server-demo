package com.cui.netty_server_demo;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 字节转换工具类
 * 
 * @author cuipengfei
 * 
 */
public class Converter {

	/**
	 * 把无符号32位long转换为字节数组
	 * 
	 * @param val
	 * @return
	 */
	public static byte[] unSigned32LongToBigBytes(long val) {
		byte[] b = new byte[4];
		b[3] = (byte) (val >> 0);
		b[2] = (byte) (val >> 8);
		b[1] = (byte) (val >> 16);
		b[0] = (byte) (val >> 24);
		return b;
	}

	/**
	 * 把字节数组转换为无符号32位long
	 * 
	 * @param b
	 * @param pos
	 * @return
	 */
	public static long bigBytes2Unsigned32Long(byte[] b, int pos) {
		int firstByte = 0;
		int secondByte = 0;
		int thirdByte = 0;
		int fourthByte = 0;
		int index = pos;
		firstByte = (0x000000FF & ((int) b[index + 3]));
		secondByte = (0x000000FF & ((int) b[index + 2]));
		thirdByte = (0x000000FF & ((int) b[index + 1]));
		fourthByte = (0x000000FF & ((int) b[index + 0]));
		index = index + 4;
		return ((long) (fourthByte << 24 | thirdByte << 16 | secondByte << 8 | firstByte)) & 0xFFFFFFFFL;
	}

}
