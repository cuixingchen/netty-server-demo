package com.cui.netty_server_demo;

import java.nio.ByteBuffer;


/**
 * 消息
 * @author cuipengfei
 *
 */
public class Msg {

	private long userId;//32位大端
	private long passWord;//32位大端
	
	/**
	 * 序列化
	 * @return
	 */
	public byte[] bodytoBytes(){
		byte[] data = new byte[4+4];
		int offset = 0;
		System.arraycopy(Converter.unSigned32LongToBigBytes(userId), 0, data, offset, 4);
		offset+=4;
		System.arraycopy(Converter.unSigned32LongToBigBytes(passWord), 0, data, offset, 4);
		return encode(data);//编码转义
	}
	/**
	 * 反序列化
	 * @return
	 */
	public boolean bodyfromBytes(byte[] b){
		b=decode(b);//解码转义
		boolean resultState = false;
		int offset = 0;
		try {
			userId = Converter.bigBytes2Unsigned32Long(b, offset);
			offset+=4;
			passWord=Converter.bigBytes2Unsigned32Long(b, offset);
			resultState = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultState;
	}
	
	/**
	 * 编码转义
	 * 
	 * @param bytes
	 * @return
	 */
	private byte[] encode(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		buffer.position(0);
		buffer.put((byte) 0x5b);
		for (byte b : bytes) {
			if (b == 0x5b) {
				buffer.put((byte) 0x5a);
				buffer.put((byte) 0x01);
			} else if (b == 0x5a) {
				buffer.put((byte) 0x5a);
				buffer.put((byte) 0x02);
			} else if (b == 0x5d) {
				buffer.put((byte) 0x5e);
				buffer.put((byte) 0x01);
			} else if (b == 0x5e) {
				buffer.put((byte) 0x5e);
				buffer.put((byte) 0x02);
			} else {
				buffer.put(b);
			}
		}
		buffer.put((byte) 0x5d);
		byte[] result = new byte[buffer.position()];
		buffer.position(0);
		buffer.get(result);
		return result;
	}
	/**
	 * 解码转义
	 * 
	 * @param b
	 * @return
	 */
	private byte[] decode(byte[] b) {
		ByteBuffer buffer = ByteBuffer.allocate(10 * 1024 * 1024);
		ByteBuffer buffer1 = ByteBuffer.wrap(b);
		buffer.position(0);
		while (buffer1.remaining() > 0) {

			byte d = buffer1.get();
			if (d == 0x5a) {
				byte e = buffer1.get();
				if (e == 0x02)
					buffer.put((byte) 0x5a);
				else if (e == 0x01)
					buffer.put((byte) 0x5b);
			} else if (d == 0x5e) {
				byte e = buffer1.get();
				if (e == 0x02)
					buffer.put((byte) 0x5e);
				else if (e == 0x01)
					buffer.put((byte) 0x5d);
			} else {
				buffer.put(d);
			}
		}

		byte[] result = new byte[buffer.position()];
		buffer.position(0);
		buffer.get(result);
		return result;
	}
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getPassWord() {
		return passWord;
	}

	public void setPassWord(long passWord) {
		this.passWord = passWord;
	}
	@Override
	public String toString() {
		return "Msg [userId=" + userId + ", passWord=" + passWord + "]";
	}

}
