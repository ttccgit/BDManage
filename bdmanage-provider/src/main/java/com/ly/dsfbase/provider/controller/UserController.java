/**
 * Creation Date:2015年12月24日-下午12:18:47
 * 
 * Copyright 2008-2015 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.provider.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ly.dsfbase.service.constant.Constants;
import com.ly.dsfbase.service.constant.HResult;
import com.ly.dsfbase.service.constant.RequestVO;
import com.ly.dsfbase.service.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.google.common.base.Strings;
import com.ly.dsfbase.service.entity.User;
import com.ly.dsfbase.service.service.ComponentTest;
import com.ly.dsfbase.service.service.IUserService;

/**
 * Demo<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2015年12月24日-下午12:18:47
 * @since 2015年12月24日-下午12:18:47
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IUserService userService;
	
	@Autowired
	private ComponentTest componentService;

	/**
	 * 查询用户列表
	 * @return
	 */
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	@ResponseBody
	public HResult authenticate(Model model, @RequestBody RequestVO requestVO){
		HResult result = new HResult();
		try {
			Map<String, Object> mapParameter = (Map<String, Object>) requestVO.getData();
			result.setData(new Member(10000,"cody"));
		} catch (Exception e) {
			result.setErrcode(Constants.ERROR);
			result.setErrmsg(e.getLocalizedMessage());
		}
		return result;
	}




	/**
	 * 查询用户列表
	 * @return
	 */
	@RequestMapping(value = "/userList", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> searchUsers() {
		//响应对象
		ResponseEntity<Object> response = null;
		
		//响应体内容
		Map<String, Object> bodyResult = new HashMap<>(2);
		try {
			List<User> list = this.userService.queryUserList();
			
			bodyResult.put("success", true);
			bodyResult.put("datas", list);
			
			//设置响应体内容
			response = new ResponseEntity<Object>(bodyResult, HttpStatus.OK);
			
		} catch (Exception e) {
			logger.error("查询用户列表失败!", e);
			
			bodyResult.put("success", false);
			bodyResult.put("msg", "服务器出错");
			
			//设置响应体内容
			response = new ResponseEntity<Object>(bodyResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	/**
	 * 查询单个用户
	 * @return
	 */
	@RequestMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> searchUser(@RequestParam("id") long id) {
		//响应对象
		ResponseEntity<Object> response = null;
		
		//响应体内容
		Map<String, Object> bodyResult = new HashMap<>(2);
		
		try {
			User user = this.userService.queryUser(id);
			
			bodyResult.put("success", true);
			bodyResult.put("datas", user);
			
			//设置响应体内容
			response = new ResponseEntity<Object>(bodyResult, HttpStatus.OK);
			
		} catch (Exception e) {
			logger.error("查询用户失败!", e);
			
			bodyResult.put("success", false);
			bodyResult.put("msg", "服务器出错");
			
			//设置响应体内容
			response = new ResponseEntity<Object>(bodyResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	/**
	 * 新增用户
	 * @return
	 */
	@RequestMapping(value = "/addUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addUser(
			//若是将参数放入request body中则使用@RequestBody 获取body内容，然后再解析
			@RequestBody(required = false) String requestBody,
			
			//若是将参数放入表单中则使用@RequestParam 获取参数内容
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "age", required = false, defaultValue = "-1") int age,
			@RequestParam(value = "remark", required = false) String remark) {
		
		//响应对象
		ResponseEntity<Object> response = null;
		
		//响应体内容
		Map<String, Object> bodyResult = new HashMap<>(2);
		
		try {
			if(Strings.isNullOrEmpty(name)) {
				bodyResult.put("success", false);
				bodyResult.put("msg", "名称不能为空");
			} else if(age == -1) {
				bodyResult.put("success", false);
				bodyResult.put("msg", "年龄不能为空");
			} else {
				this.userService.addUser(name, sex, age, remark);
				bodyResult.put("success", true);
			}
			
			//设置响应体内容
			response = new ResponseEntity<Object>(bodyResult, HttpStatus.OK);
			
		} catch (Exception e) {
			logger.error("添加用户失败!", e);
			
			bodyResult.put("success", false);
			bodyResult.put("msg", "服务器出错");
			
			//设置响应体内容
			response = new ResponseEntity<Object>(bodyResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	/**
	 * 更新用户
	 * @return
	 */
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateUser(
			//若是将参数放入request body中则使用@RequestBody 获取body内容，然后再解析
			@RequestBody(required = false) String requestBody,
			
			//若是将参数放入表单中则使用@RequestParam 获取参数内容
			@RequestParam(value = "id", required = false, defaultValue = "-1") long id,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "age", required = false, defaultValue = "-1") int age,
			@RequestParam(value = "remark", required = false) String remark) {
		
		//响应对象
		ResponseEntity<Object> response = null;
		
		//响应体内容
		Map<String, Object> bodyResult = new HashMap<>(2);
		
		try {
			if(id == -1) {
				bodyResult.put("success", false);
				bodyResult.put("msg", "ID不能为空");
			} else if(Strings.isNullOrEmpty(name)) {
				bodyResult.put("success", false);
				bodyResult.put("msg", "名称不能为空");
			} else if(age == -1) {
				bodyResult.put("success", false);
				bodyResult.put("msg", "年龄不能为空");
			} else {
				this.userService.updateUser(id, name, age, remark);
				bodyResult.put("success", true);
			}
			
			//设置响应体内容
			response = new ResponseEntity<Object>(bodyResult, HttpStatus.OK);
			
		} catch (Exception e) {
			logger.error("更新用户失败!", e);
			
			bodyResult.put("success", false);
			bodyResult.put("msg", "服务器出错");
			
			//设置响应体内容
			response = new ResponseEntity<Object>(bodyResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	/**
	 * 删除用户
	 * @return
	 */
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteUser(
			//若是将参数放入request body中则使用@RequestBody 获取body内容，然后再解析
			@RequestBody(required = false) String requestBody,
			
			//若是将参数放入表单中则使用@RequestParam 获取参数内容
			@RequestParam(value = "id", required = false, defaultValue = "-1") Long id) {
		
		//响应对象
		ResponseEntity<Object> response = null;
		
		//响应体内容
		Map<String, Object> bodyResult = new HashMap<>(2);
		
		try {
			if(id == -1) {
				bodyResult.put("success", false);
				bodyResult.put("msg", "ID不能为空");
			} else {
				this.userService.deleteUser(id);
				bodyResult.put("success", true);
			}
			
			//设置响应体内容
			response = new ResponseEntity<Object>(bodyResult, HttpStatus.OK);
			
		} catch (Exception e) {
			logger.error("更新用户失败!", e);
			
			bodyResult.put("success", false);
			bodyResult.put("msg", "服务器出错");
			
			//设置响应体内容
			response = new ResponseEntity<Object>(bodyResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	/**
	 * 测试工具类
	 * @return
	 */
	@RequestMapping(value = "/testTool")
	public void testTool() {
		try {
			this.componentService.test();
		} catch (MQClientException | RemotingException | MQBrokerException
				| InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

