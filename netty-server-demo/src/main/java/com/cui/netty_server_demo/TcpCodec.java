package com.cui.netty_server_demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.ReferenceCountUtil;

import java.nio.ByteBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编解码
 * 
 * @author cuipengfei
 *
 */
public class TcpCodec extends ByteToMessageCodec<Msg> {

	private static final byte HeadFlag = 0x5b;
	private static final byte EndFlag = 0x5d;
	private static final Logger logger = LoggerFactory
			.getLogger(TcpCodec.class);

	ByteBuffer bf = ByteBuffer.allocate(10 * 1024 * 1024);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
			List<Object> out) throws Exception {
		try {
			while (buffer.readableBytes() > 0) {
				byte b = buffer.readByte();
				bf.put(b);
				if (b == EndFlag) {
					byte[] bytes = new byte[bf.position() - 2];
					byte[] msgbytes = new byte[bf.position()];
					if (bf.get(0) == HeadFlag) {
						bf.position(1);
						bf.get(bytes);
						bf.position(0);
						bf.get(msgbytes);
						logger.debug("接受到原始数据："
								+ HexStringUtil.Bytes2HexString(msgbytes));
						out.add(bytes);
					}
					bf.clear();
				}
			}
		} catch (Exception e) {

			logger.error("解码异常:" + e.toString());

		} finally {
			ReferenceCountUtil.release(buffer);
		}

	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf out)
			throws Exception {
		byte[] bt = msg.bodytoBytes();
		out.writeBytes(bt);
		logger.debug("发送原始数据：" + HexStringUtil.Bytes2HexString(bt));

	}

}
