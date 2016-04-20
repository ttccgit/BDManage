/**
 * Creation Date:2016年3月9日-上午11:08:47
 * 
 * Copyright 2008-2016 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ly.dsfbase.service.entity.User;

/**
 * Demo <br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2016年3月9日-上午11:08:47
 * @since 2016年3月9日-上午11:08:47
 */
@Repository
public interface UserDao {
	
	public int insertUser(User user);
	
	public User selectUser(long id);
	
	public List<User> selectUsers();
	
	public int updateUser(User user);
	
	public int deleteUser(long id);
}

