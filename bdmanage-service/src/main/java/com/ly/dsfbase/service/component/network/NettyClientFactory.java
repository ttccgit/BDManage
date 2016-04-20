/**
 * Creation Date:2016年3月14日-上午10:32:17
 * 
 * Copyright 2008-2016 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.component.network;

/**
 * Netty Client Factory<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2016年3月14日-上午10:32:17
 * @since 2016年3月14日-上午10:32:17
 */
public class NettyClientFactory {
	
	/**
	 * 创建NettyClient对象
	 * @param host
	 * @param port
	 * @return
	 * @throws InterruptedException
	 */
	public NettyClient create(String host, int port) throws InterruptedException {
		NettyClient nettyClient = new NettyClient();
		nettyClient.start(host, port);
		return nettyClient;
	}
}

