/**
 * Creation Date:2016年3月9日-上午11:20:25
 * 
 * Copyright 2008-2016 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ly.dsfbase.service.dao.UserDao;
import com.ly.dsfbase.service.entity.User;

/**
 * Demo <br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2016年3月9日-上午11:20:25
 * @since 2016年3月9日-上午11:20:25
 */
@Service("userService")
public class UserServiceImpl implements IUserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Resource
	private UserDao userDao;
		
	@Override
	public List<User> queryUserList() {
		return this.userDao.selectUsers();
	}

	@Override
	public User queryUser(long id) {
		return this.userDao.selectUser(id);
	}

	@Override
	public void updateUser(long id, String name, int age, String remark) {
		User user = this.userDao.selectUser(id);
		if(user != null) {
			user.setAge(age);
			user.setName(name);
			user.setRemark(remark);
			this.userDao.updateUser(user);
		}
	}

	@Override
	public void deleteUser(long id) {
		this.userDao.deleteUser(id);
	}

	@Override
	public void addUser(String name, String sex, int age, String remark) {
		User user = new User();
		user.setAge(age);
		user.setName(name);
		user.setSex(sex);
		user.setRemark(remark);
		
		this.userDao.insertUser(user);
	}

}

