package com.ly.dsfbase.service.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	private final static RequestConfig requestConfig = RequestConfig.custom()
			.setSocketTimeout(5000)
			.setConnectTimeout(5000)
			.setConnectionRequestTimeout(10000)
			.build();// 设置请求和传输超时时间
	
	private static PoolingHttpClientConnectionManager phccm = new PoolingHttpClientConnectionManager();

	/**
	 * HTTP GET
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		return get(url, null, null);
	}
	
	/**
	 * HTTP GET请求
	 * @param uri
	 * @return
	 */
	public static String get(String url, Map<String, String> headers, String charset) {
		if(charset == null || charset.trim().isEmpty()) {
			charset = "UTF-8";
		}
		
		CloseableHttpResponse response = null;
		HttpEntity entity = null;

		phccm.setMaxTotal(800);// 连接池最大并发连接数
		phccm.setDefaultMaxPerRoute(400);// 单路由最大并发数

		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManager(phccm)
				.setRetryHandler(new DefaultHttpRequestRetryHandler())
				.build();
		
		HttpGet httpGet = new HttpGet(url);

		// 设置HTTP头信息
		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				Header header = new BasicHeader(entry.getKey(), entry.getValue());
				httpGet.addHeader(header);
			}
		} else {
			Header header = new BasicHeader(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
			httpGet.addHeader(header);
		}
		
		try {
			response = httpclient.execute(httpGet);
			entity = response.getEntity();
			
			return EntityUtils.toString(entity, charset);
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (ParseException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			if (entity != null) {
				try {
					EntityUtils.consume(entity);
				} catch (IOException e) {
					logger.error("", e);
				}
			}
				
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
			
			httpGet.releaseConnection();
		}
		
		return null;
	}
	
	/**
	 * HTTP POST 请求
	 * @param url
	 * @param formData
	 * @return
	 */
	public static String post(String url, Map<String, String> formData) {
		return post(url, null, formData, null);
	}
	
	/**
	 * HTTP POST 请求
	 * @param url
	 * @param formData
	 * @return
	 */
	public static String post(String url, String reqBodyStr) {
		return post(url, null, reqBodyStr, null);
	}
	
	/**
	 * HTTP POST请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, String> headers,
			Map<String, String> formData, String charset) {
		if(charset == null || charset.trim().isEmpty()) {
			charset = "UTF-8";
		}
		
		CloseableHttpResponse response = null;
		HttpEntity entity = null;

		phccm.setMaxTotal(800);// 连接池最大并发连接数
		phccm.setDefaultMaxPerRoute(400);// 单路由最大并发数

		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManager(phccm)
				.setRetryHandler(new DefaultHttpRequestRetryHandler())
				.build();
		
		HttpPost httpPost = new HttpPost(url);
		
		// 设置HTTP头信息
		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				Header header = new BasicHeader(entry.getKey(), entry.getValue());
				httpPost.addHeader(header);
			}
		} else {
			Header header = new BasicHeader(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
            httpPost.addHeader(header);
		}
		
		//设置参数
		if (formData != null && !formData.isEmpty()) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> entry : formData.entrySet()) {
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(params));
			} catch (UnsupportedEncodingException e) {
				logger.error("", e);
			}
		}
		
		try {
			response = httpclient.execute(httpPost);
			entity = response.getEntity();
			
			return EntityUtils.toString(entity, charset);
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (ParseException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			if (entity != null) {
				try {
					EntityUtils.consume(entity);
				} catch (IOException e) {
					logger.error("", e);
				}
			}
				
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
			
			httpPost.releaseConnection();
		}
		
		return null;
		
	}
	
	/**
	 * HTTP POST请求
	 * @param url
	 * @param requestBody
	 * @return
	 */
	public static String post(String url, Map<String, String> headers,
			String reqBodyStr, String charset) {
		if(charset == null || charset.trim().isEmpty()) {
			charset = "UTF-8";
		}
		
		CloseableHttpResponse response = null;
		HttpEntity entity = null;

		phccm.setMaxTotal(800);// 连接池最大并发连接数
		phccm.setDefaultMaxPerRoute(400);// 单路由最大并发数

		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManager(phccm)
				.setRetryHandler(new DefaultHttpRequestRetryHandler())
				.build();
		
		HttpPost httpPost = new HttpPost(url);
		
		// 设置HTTP头信息
		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				Header header = new BasicHeader(entry.getKey(), entry.getValue());
				httpPost.addHeader(header);
			}
		} else {
			Header header = new BasicHeader(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
            httpPost.addHeader(header);
		}
		
		StringEntity reqBodyParams = new StringEntity(reqBodyStr, charset);
		reqBodyParams.setContentType("application/json");
        httpPost.setEntity(reqBodyParams);
        
        try {
			response = httpclient.execute(httpPost);
			entity = response.getEntity();
			return EntityUtils.toString(entity, charset);
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (ParseException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			if (entity != null) {
				try {
					EntityUtils.consume(entity);
				} catch (IOException e) {
					logger.error("", e);
				}
			}
				
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
			
			httpPost.releaseConnection();
		}
		
		return null;
	}

}
