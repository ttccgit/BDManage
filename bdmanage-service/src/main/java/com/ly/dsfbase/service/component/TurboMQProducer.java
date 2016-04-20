/**
 * Creation Date:2016年3月9日-下午4:57:12
 * 
 * Copyright 2008-2016 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.component;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.ly.dsfbase.service.constant.DFSConstants;

/**
 * TurboMQ生产者组件<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2016年3月9日-下午4:57:12
 * @since 2016年3月9日-下午4:57:12
 */
public class TurboMQProducer {
	
	/**
	 * 生产者组名
	 */
	private String producerGroup;
	
	/**
	 * nameserver地址
	 */
	private String turbomqNamesrvAddrs;
	
	/**
	 * 生成者客户端
	 */
	private DefaultMQProducer producer;

	/**
	 * 启动客户端
	 * @throws MQClientException
	 */
	public void start() throws MQClientException {
		this.producer = new DefaultMQProducer(producerGroup);
		this.producer.setNamesrvAddr(turbomqNamesrvAddrs);
		this.producer.start();
	}
	
	/**
	 * 同步发送消息
	 * @param topic
	 * @param msg
	 * @return
	 * @throws MQClientException
	 * @throws RemotingException
	 * @throws MQBrokerException
	 * @throws InterruptedException
	 */
	public SendResult sendMsg(String topic, String msg)
			throws MQClientException, RemotingException, MQBrokerException,
			InterruptedException {
		return this.producer.send(new Message(topic, msg.getBytes(DFSConstants.SYSTEM_CHARSET)));
	}
	
	/**
	 * 异步发送消息
	 * @param topic
	 * @param msg
	 * @param callBack
	 * @throws MQClientException
	 * @throws RemotingException
	 * @throws InterruptedException
	 */
	public void sendMsg(String topic, String msg, SendCallback callBack)
			throws MQClientException, RemotingException, InterruptedException {
		this.producer.send(new Message(topic, msg.getBytes(DFSConstants.SYSTEM_CHARSET)), callBack);
	}
	
	/**
	 * 关闭客户端
	 */
	public void shutdown() {
		this.producer.shutdown();
	}

	public String getProducerGroup() {
		return producerGroup;
	}

	public void setProducerGroup(String producerGroup) {
		this.producerGroup = producerGroup;
	}

	public String getTurbomqNamesrvAddrs() {
		return turbomqNamesrvAddrs;
	}

	public void setTurbomqNamesrvAddrs(String turbomqNamesrvAddrs) {
		this.turbomqNamesrvAddrs = turbomqNamesrvAddrs;
	}
	
}

