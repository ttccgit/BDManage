/**
 * Creation Date:2016年3月14日-上午10:59:03
 * 
 * Copyright 2008-2016 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.component.network;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * NettyClient 对象池<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2016年3月14日-上午10:59:03
 * @since 2016年3月14日-上午10:59:03
 */
public class NettyClientPool {
	
	/**
	 * 存放NettyClient的池
	 */
	private LinkedBlockingQueue<NettyClient> pool = new LinkedBlockingQueue<>();

	/**
	 * 服务器IP地址
	 */
	private String host;
	
	/**
	 * 服务器端口
	 */
	private int port;
	
	/**
	 * 客户端最大连接数
	 */
	private AtomicInteger maxConnections;
	
	/**
	 * 初始连接大小
	 */
	private int initialConns;
	
	/**
	 * 客户端当前存活的连接
	 */
	private AtomicInteger activeNums = new AtomicInteger(0);
	
	private NettyClientFactory nettyClientFactory;
	
	/**
	 * 锁
	 */
	private ReentrantLock lock = new ReentrantLock();
	
	/**
	 * 定时检查连接是否有效
	 */
	private Timer timer = new Timer("NettyClientPool-Check", true);
	
	public NettyClientPool(String host, int port, int maxConns, int initialConns) {
		this.host = host;
		this.port = port;
		
		if(maxConns < 1) {
			maxConns = 20;
		}
		
		if(initialConns < 1) {
			initialConns = 10;
		}
		
		
		this.initialConns = initialConns;
		this.maxConnections = new AtomicInteger(maxConns);
		this.nettyClientFactory = new NettyClientFactory();
		
		//初始化1个连接
		this.init(this.initialConns);
		
		//定时任务
		this.scheduleCheck();
	}
	
	public NettyClient borrowObject() throws InterruptedException {
		if(!this.pool.isEmpty()) {
			return this.pool.take();
		} else {
			this.lock.lock();
			try {
				if(this.activeNums.get() >= this.maxConnections.get()) {
					return this.pool.take();
				} else {
					NettyClient nettyClient = this.nettyClientFactory.create(host, port);
					this.pool.put(nettyClient);
					this.activeNums.incrementAndGet();
					return this.pool.take();
				}
				
			} finally {
				this.lock.unlock();
			}
		}
	}
	
	public void returnObject(NettyClient client) throws InterruptedException {
		client.setCallback(null);
		this.pool.put(client);
	}
	
	public void destroy() {
		NettyClient client = NettyClientPool.this.pool.poll();
		while(client != null) {
			if(client.isAlive() || client.isOpen()) {
				client.close();
			}
			
			client = NettyClientPool.this.pool.poll();
		}
		
		this.timer.cancel();
	}
	
	/**
	 * 初始化连接池
	 * @throws InterruptedException
	 */
	private void init(int size) {
		try {
			NettyClient nettyClient;
			for(int i = 0; i < size; i++) {
				nettyClient = this.nettyClientFactory.create(host, port);
				this.pool.put(nettyClient);
				this.activeNums.incrementAndGet();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 定时检查连接是否有效
	 */
	private void scheduleCheck() {
		this.timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				try {
					NettyClient client = NettyClientPool.this.pool.poll();
					while(client != null) {
						if(client.isAlive()) {
							NettyClientPool.this.pool.offer(client);
							
						} else {
							NettyClientPool.this.activeNums.decrementAndGet();
							
							if(client.isOpen()) {
								client.close();
							}

							client = null;
						}
						
						client = NettyClientPool.this.pool.poll();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 3000, 3000);
	}
}

