package com.cui.netty_server_demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * tcp服务端
 * 
 * @author cuipengfei
 *
 */
public class TcpServer extends Thread {

	private static TcpServer tcpServer;

	public static TcpServer getInstance() {
		if (tcpServer == null) {
			synchronized (TcpServer.class) {
				if (tcpServer == null) {
					tcpServer = new TcpServer();
				}
			}
		}
		return tcpServer;
	}

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TcpServer.class);

	int hostport; // 服务器端口
	LogLevel loglevel;

	@Override
	public void run() {
		// 初始化服务器
		init();
		// 启动临时客户端管理(秒)
		// 启动消息处理
		// 启动客户端管理
	}

	/**
	 * 初始化
	 */
	private void init() {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			this.hostport = 21108;
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class) // (3)
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
								@Override
								public void initChannel(SocketChannel ch)
										throws Exception {
									ch.pipeline().addLast(new TcpCodec(),

									new TcpServerHandler());
								}
							}).option(ChannelOption.SO_BACKLOG, 128) // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			// Bind and start to accept incoming connections.
			ChannelFuture f;
			f = b.bind(hostport).sync();

			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to
			// gracefully
			// shut down your server.
			// f.channel().closeFuture().sync();
		} catch (InterruptedException e) {

		} finally {
			// workerGroup.shutdownGracefully();
			// bossGroup.shutdownGracefully();
		}
		logger.info("server初始化完成");

	}

	/**
	 * 发送消息
	 * 
	 * @param m
	 */
	public void send(ChannelHandlerContext chtx, Msg m) {
		if (chtx != null && chtx.channel().isOpen()) {
			logger.info("server发送消息：" + m);
			chtx.write(m);
			chtx.flush();
		}
	}
}
