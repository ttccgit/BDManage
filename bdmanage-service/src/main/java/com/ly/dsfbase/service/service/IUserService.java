/**
 * Creation Date:2016年3月9日-上午11:18:31
 * 
 * Copyright 2008-2016 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.service;

import java.util.List;

import com.ly.dsfbase.service.entity.User;

/**
 * Demo <br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2016年3月9日-上午11:18:31
 * @since 2016年3月9日-上午11:18:31
 */
public interface IUserService {
	
	public List<User> queryUserList();
	
	public User queryUser(long id);
	
	public void addUser(String name, String sex, int age, String remark);
	
	public void updateUser(long id, String name, int age, String remark);
	
	public void deleteUser(long id);
}

