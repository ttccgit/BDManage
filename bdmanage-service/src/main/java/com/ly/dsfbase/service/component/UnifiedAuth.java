/**
 * Creation Date:2016年3月10日-上午10:24:45
 * 
 * Copyright 2008-2016 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.component;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.ly.dsfbase.service.util.HttpUtils;

/**
 * 统一权限认证组件<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2016年3月10日-上午10:24:45
 * @since 2016年3月10日-上午10:24:45
 */
public class UnifiedAuth {
	
	private static final Logger logger = LoggerFactory.getLogger(UnifiedAuth.class);
	
	/**
	 * 认证并获取用户基本信息
	 * @param userName
	 * @param password
	 * @param authUrl
	 * @param projCode
	 * @return
	 */
	public static Map<String, String> auth(String userName, String password, String authUrl, String projCode) {
		String xmlResp = HttpUtils.post(authUrl, null, buildReqBody(userName, password, projCode), "gb2312");
		return parseUserInfo(xmlResp);
	}

	/**
	 * 生成请求体
	 * @param userName
	 * @param password
	 * @return
	 */
	private static String buildReqBody(String userName, String password, String projCode) {
		StringBuffer info = new StringBuffer();
		info.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
			.append("<meminfo>")
			.append("<trasinf>")
			.append("<service>login</service>")
			.append("<projCode>")
			.append(projCode)
			.append("</projCode>")
			.append("</trasinf>")
			.append("<ucom>")
			.append("<jobNumber>")
			.append(userName)
			.append("</jobNumber>")
			.append("<numType>1</numType>")
			.append("<pwd>")
			.append(password)
			.append("</pwd>")
			.append("</ucom>")
			.append("</meminfo>");
		
		return info.toString();
	}
	
	/**
	 * 解析登录返回结果
	 * @param xml
	 * @return
	 */
	private static Map<String, String> parseUserInfo(String xml) {
		Map<String, String> rs = new HashMap<>();
		
		if(Strings.isNullOrEmpty(xml)) {
			rs.put("errorInfo", "认证失败!");
			return rs;
		}
		
		try {
			Document document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();
			
			String errorInfo = root.elementText("errInfo");
			if(Strings.isNullOrEmpty(errorInfo)) {
				Element userInfo = root.element("detailInfo").element("userInfo");
				
				rs.put("userId", userInfo.elementText("userId"));
				rs.put("name", userInfo.elementText("name"));
				rs.put("jobNumber", userInfo.elementText("jobNumber"));
				rs.put("departName", userInfo.elementText("departName"));
				rs.put("email", userInfo.elementText("email"));
				rs.put("dptid", userInfo.elementText("dptid"));
			} else {
				rs.put("errorInfo", errorInfo);
			}
			
		} catch (Exception e) {
			logger.error("解析认证结果错误!", e);
			
			rs.put("errorInfo", "解析认证结果错误!");
		}
		
		return rs;
	}
	
}

