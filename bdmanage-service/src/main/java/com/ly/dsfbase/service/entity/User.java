/**
 * Creation Date:2015年12月22日-下午4:58:39
 * 
 * Copyright 2008-2015 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.entity;

import java.io.Serializable;

/**
 * Demo<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2015年12月22日-下午4:58:39
 * @since 2015年12月22日-下午4:58:39
 */
public class User implements Serializable {
	private static final long serialVersionUID = 6509285476388099923L;
	
	private long id;
	private String name;
	private String sex;
	private int age;
	private String remark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
