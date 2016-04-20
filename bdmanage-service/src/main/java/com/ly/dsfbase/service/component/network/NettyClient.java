/**
 * Creation Date:2016年3月11日-下午4:58:52
 * 
 * Copyright 2008-2016 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.component.network;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Strings;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Netty Client<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2016年3月11日-下午4:58:52
 * @since 2016年3月11日-下午4:58:52
 */
public class NettyClient {
	
	private EventLoopGroup group;
	
    private Channel channel;
    
    private SocketCallback callback;
    
    /**
     * 启动客户端
     * @param host
     * @param port
     * @param callback
     * @throws InterruptedException
     */
    public void start(String host, int port) throws InterruptedException {
    	group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ClientInitializer());

		// 连接服务端
		this.channel = bootstrap.connect(host, port).sync().channel();
    }
    
    /**
     * 关闭客户端
     */
    public void close() {
    	this.channel.close();
    	this.group.shutdownGracefully();
    }
    
    /**
     * 异步发送消息
     * @param msg
     */
    public void send(String msg, SocketCallback callback) {
    	this.callback = callback;
    	this.channel.writeAndFlush(msg);
    }
    
    /**
     * 同步发送消息
     * @param msg
     * @return
     * @throws InterruptedException
     */
    public String send(String msg) throws InterruptedException {
    	final CountDownLatch countDownLatch = new CountDownLatch(1);
    	
    	final Map<String, String> rs = new HashMap<>(1);
    	
    	this.callback = new SocketCallback() {
			
			@Override
			public void callback(String msg) throws Exception {
				rs.put("msg", msg);
				countDownLatch.countDown();
			}
		};
		
		this.channel.writeAndFlush(msg);
		
		countDownLatch.await(2000, TimeUnit.MILLISECONDS);
		
		String response = rs.get("msg");
		if(Strings.isNullOrEmpty(response)) {
			throw new RuntimeException("请求超时");
		}
		
		return response;
    }
    
    private class ClientInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();

            pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
            pipeline.addLast("decoder", new StringDecoder());
            pipeline.addLast("encoder", new StringEncoder());
            
            // 客户端的逻辑
            pipeline.addLast("handler", new ClientHandler());
        }
    }
    
    private class ClientHandler extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        	NettyClient.this.callback.callback(msg);
        }
        
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
        }
    }
    
    public boolean isAlive() {
    	return (this.channel != null && this.channel.isActive());
    }
    
    public boolean isOpen() {
    	return (this.channel != null && this.channel.isOpen());
    }

	public SocketCallback getCallback() {
		return callback;
	}

	public void setCallback(SocketCallback callback) {
		this.callback = callback;
	}
}

