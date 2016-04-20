/**
 * Creation Date:2016年3月11日-下午5:08:06
 * 
 * Copyright 2008-2016 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.component.network;

/**
 * 用于netty回调函数<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2016年3月11日-下午5:08:06
 * @since 2016年3月11日-下午5:08:06
 */
public interface SocketCallback {
	
	public void callback(String msg) throws Exception;
}

