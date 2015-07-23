package com.cui.netty_server_demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * tcp handler
 * 
 * @author cuipengfei
 *
 */
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(TcpServerHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object obj) { // (2)
		try {
			if (obj instanceof byte[]) {
				final byte[] msgbytes = (byte[]) obj;
				Msg msg=new Msg();
				msg.bodyfromBytes(msgbytes);
				logger.info("server收到消息内容:"+msg);
				msg.setUserId(200);
				msg.setPassWord(201);
				TcpServer.getInstance().send(ctx, msg);
			} else {
				logger.error("server主handler---消息解码有误，请检查！！");
			}

		} finally {
			ReferenceCountUtil.release(obj);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		logger.info("-------------server临时客户端建立连接--------------");
		//添加临时客户端

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub

		InetSocketAddress address = (InetSocketAddress) ctx.channel()
				.remoteAddress();
		InetAddress inetAdd = address.getAddress();
		logger.info("server客户端断开连接：" + ctx.name() + ",IP:" + inetAdd.getHostAddress()
				+ ",port:" + address.getPort());
		// 记录日志
		//移除临时客户端和客户端
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.

		logger.error("server主handle---rexceptionCaught异常", cause);
		ctx.close();
		ctx = null;
	}

}
