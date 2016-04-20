/**
 * Creation Date:2016年3月9日-下午7:19:29
 * 
 * Copyright 2008-2016 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.service;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.ly.dsfbase.service.component.ImgClient;
import com.ly.dsfbase.service.component.RedisShardedPool;
import com.ly.dsfbase.service.component.TMQConsumerCallback;
import com.ly.dsfbase.service.component.TurboMQConsumer;
import com.ly.dsfbase.service.component.TurboMQProducer;
import com.ly.dsfbase.service.constant.DFSConstants;

import redis.clients.jedis.ShardedJedis;

/**
 * Demo<br>
 * 
 * 测试各个组件用法
 * 
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2016年3月9日-下午7:19:29
 * @since 2016年3月9日-下午7:19:29
 */
@Service
public class ComponentTest implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(ComponentTest.class);
	
	@Autowired
	private RedisShardedPool redis;
	
	@Autowired
	private TurboMQConsumer consumer;
	
	@Autowired
	private TurboMQProducer producer;
	
	@Autowired
	private ImgClient imgClient;
	
	/**
	 * 类加载时启动生产者、消费者客户端组件，同一个生产者、消费者客户端类只能启动一次，关闭一次
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
//		this.testTMQConsumer();
//		producer.start();
	}
	
	public void test() throws MQClientException, RemotingException,
			MQBrokerException, InterruptedException {
//		this.testRedis();
//		this.testTMQProducer();
		this.testImgClient();
		
	}
	
	/**
	 * 测试消息队列生产者
	 * @throws MQClientException
	 * @throws RemotingException
	 * @throws MQBrokerException
	 * @throws InterruptedException
	 */
	public void testTMQProducer() throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		logger.info("test TMQProducer...");
		
		producer.sendMsg("DSF_T1", "测试DSF1");
		producer.sendMsg("DSF_T1", "测试DSF2");
	}
	
	/**
	 * 测试Turbomq消费者
	 * @throws MQClientException
	 */
	public void testTMQConsumer() throws MQClientException {
		logger.info("test TMQConsumer...");
		
		this.consumer.start(new TMQConsumerCallback() {
			
			@Override
			public void receive(List<MessageExt> msgs) {
				logger.info("test TMQConsumer...");
				
				for(MessageExt msg : msgs) {
					logger.info(new String(msg.getBody(), DFSConstants.SYSTEM_CHARSET));
				}
			}
		});
	}

	/**
	 * 测试redis使用
	 */
	public void testRedis() {
		logger.info("test redis...");
		
		ShardedJedis jedis = redis.getJedis();
		jedis.set("DSF_T1", "hello");
		redis.returnResource(jedis);
		
		logger.info(redis.getJedis().get("DSF_T1"));
	}
	
	/**
	 * 测试图片客户端
	 */
	public void testImgClient() {
		for(int i = 0; i<10;i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					String rs = null;
					try {
						rs = imgClient.uploadImg("http://192.168.20.100", "/hotel", new File("d:\\我的配置\\桌面\\ls1.jpg"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					logger.info("上传完成: " + rs);
				}
			}).start();
		}
		
		logger.info(imgClient.cutImg("http://192.168.20.100/hotel/2016/03/14/mmw1660.jpg", "/hotel", 400, 300, ImgClient.CUT_TYPE_00));
		logger.info(imgClient.cutImg("http://192.168.20.100/hotel/2016/03/14/mmw1660.jpg", "/hotel", 150, 150, ImgClient.CUT_TYPE_01));
		logger.info(imgClient.cutImg("http://192.168.20.100/hotel/2016/03/14/mmw1660.jpg", "/hotel", 150, 150, ImgClient.CUT_TYPE_02));
		logger.info(imgClient.cutImg("http://192.168.20.100/hotel/2016/03/14/mmw1660.jpg", "/hotel", 150, 150, ImgClient.CUT_TYPE_03));
	}
}

