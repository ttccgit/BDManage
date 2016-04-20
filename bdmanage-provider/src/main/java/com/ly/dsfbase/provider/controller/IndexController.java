/**
 * Creation Date:2015年12月24日-下午12:18:47
 * 
 * Copyright 2008-2015 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.provider.controller;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.google.common.base.Strings;
import com.ly.dsfbase.service.entity.User;
import com.ly.dsfbase.service.service.ComponentTest;
import com.ly.dsfbase.service.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2015年12月24日-下午12:18:47
 * @since 2015年12月24日-下午12:18:47
 */
@Controller
public class IndexController {
	
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);


	@RequestMapping(value="/index")
	public String index(Model model){
		return "index";
	}

	@RequestMapping(value="/login")
	public String login(Model model){
		return "login";
	}
}

