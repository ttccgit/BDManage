/**
 * Creation Date:2015-7-28-上午09:43:42
 * 
 * Copyright 2008-2015 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.google.common.base.Strings;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * redis 分片集群<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2015-7-28-上午09:43:42
 * @since 2015-7-28-上午09:43:42
 */
public class RedisShardedPool implements InitializingBean {

	private String shardedHost;
	
	private String shardedPort;
	
	private String password;
	
	private int timeout;
	
	private int maxIdle;
	
	private int maxTotal;
	
	private long maxWaitMillis;

	private ShardedJedisPool jedisPool = null;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		JedisPoolConfig config = new JedisPoolConfig();// Jedis池配置
		config.setMaxIdle(maxIdle);
		config.setMaxTotal(maxTotal);
		config.setMaxWaitMillis(maxWaitMillis);
		
		String[] hosts = shardedHost.split(",");
		String[] ports = shardedPort.split(",");
		String[] passwds = password.split(",");
		
		List<JedisShardInfo> jdsInfoList = new ArrayList<JedisShardInfo>(hosts.length);
		for(int i = 0; i < hosts.length; i++) {
			JedisShardInfo jsi = new JedisShardInfo(hosts[i], Integer.valueOf(ports[i]), timeout);
			
			String pwd = null;
			if(!Strings.isNullOrEmpty(password)) {
				pwd = passwds[i];
			}
			
			jsi.setPassword(pwd);
			jdsInfoList.add(jsi);
		}
		
		jedisPool = new ShardedJedisPool(config, jdsInfoList);
	}
	
	/**
     * 获取Jedis实例
     * @return
     */
    public synchronized ShardedJedis getJedis() throws RuntimeException {
        if(this.jedisPool == null) {
        	throw new RuntimeException("jedis pool not be initialized!");
        }
    	
        ShardedJedis jedis = jedisPool.getResource();
        if(jedis == null) {
			throw new RuntimeException("can not found avaliable jedis!");
		}
        
        return jedis;
    }
    
    /**
     * 释放jedis资源
     * @param jedis
     */
	public void returnResource(final ShardedJedis jedis) {
        if (jedis != null) {
        	jedis.close();
        }
    }

	public String getShardedHost() {
		return shardedHost;
	}

	public void setShardedHost(String shardedHost) {
		this.shardedHost = shardedHost;
	}

	public String getShardedPort() {
		return shardedPort;
	}

	public void setShardedPort(String shardedPort) {
		this.shardedPort = shardedPort;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
}

