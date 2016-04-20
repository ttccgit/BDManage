/**
 * Creation Date:2016年3月9日-下午5:46:28
 * 
 * Copyright 2008-2016 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.component;

import java.util.List;

import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * Turbomq消费者回调接口<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2016年3月9日-下午5:46:28
 * @since 2016年3月9日-下午5:46:28
 */
public interface TMQConsumerCallback {

	public void receive(List<MessageExt> msgs);
}

