/**
 * Creation Date:2015年12月22日-下午4:58:39
 * 
 * Copyright 2008-2015 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.entity;

import java.io.Serializable;


public class Member implements Serializable {
	private static final long serialVersionUID = 6509285476388099923L;
	
	private long id;
	private String name;
	private String sex;
	private int age;
	private String remark;
	public Member (long id,String name){
		this.id = id;
		this.name = name;
	}

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
