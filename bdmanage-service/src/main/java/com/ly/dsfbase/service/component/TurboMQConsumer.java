/**
 * Creation Date:2016年3月9日-下午4:57:12
 * 
 * Copyright 2008-2016 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.component;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.google.common.base.Strings;

/**
 * TurboMQ消费者组件<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2016年3月9日-下午4:57:12
 * @since 2016年3月9日-下午4:57:12
 */
public class TurboMQConsumer {
	
	/**
	 * 消费者组名
	 */
	private String consumerGroup;
	
	/**
	 * nameserver地址
	 */
	private String turbomqNamesrvAddrs;
	
	/**
	 * 消费者订阅的topic，多个topic之间用英文分号隔开
	 */
	private String consumerSubscribeTopics;
	
	/**
	 * 消费者 
	 */
	private DefaultMQPushConsumer consumer;
	
	/**
	 * 消费回调接口
	 */
	private TMQConsumerCallback callback;

	/**
	 * 启动客户端
	 * @throws MQClientException
	 */
	public void start(TMQConsumerCallback callback) throws MQClientException {
		this.callback = callback;
		
		this.consumer = new DefaultMQPushConsumer(consumerGroup);
		this.consumer.setNamesrvAddr(turbomqNamesrvAddrs);
		
		//设置订阅的消息topic
		if(Strings.isNullOrEmpty(consumerSubscribeTopics)) {
			throw new MQClientException("can not found subscribe topics!", null);
		}
		
		for(String topic : this.consumerSubscribeTopics.split(";")) {
			this.consumer.subscribe(topic, "*");
		}
		
		//设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费,如果非第一次启动，那么按照上次消费的位置继续消费
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		
		//设置consumeMessageBatchMaxSize参数来批量接收多条消息
		consumer.setConsumeMessageBatchMaxSize(10);
		
		//设置回调
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
					ConsumeConcurrentlyContext context) {
				
				TurboMQConsumer.this.callback.receive(msgs);

				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		
		this.consumer.start();
	}
	
	/**
	 * 关闭客户端
	 */
	public void shutdown() {
		this.consumer.shutdown();
	}

	public String getConsumerGroup() {
		return consumerGroup;
	}

	public void setConsumerGroup(String consumerGroup) {
		this.consumerGroup = consumerGroup;
	}

	public String getTurbomqNamesrvAddrs() {
		return turbomqNamesrvAddrs;
	}

	public void setTurbomqNamesrvAddrs(String turbomqNamesrvAddrs) {
		this.turbomqNamesrvAddrs = turbomqNamesrvAddrs;
	}

	public String getConsumerSubscribeTopics() {
		return consumerSubscribeTopics;
	}

	public void setConsumerSubscribeTopics(String consumerSubscribeTopics) {
		this.consumerSubscribeTopics = consumerSubscribeTopics;
	}
}

