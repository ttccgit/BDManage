/**
 * Creation Date:2016年3月11日-下午5:16:15
 * 
 * Copyright 2008-2016 © 同程网 Inc. All Rights Reserved
 */
package com.ly.dsfbase.service.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.ly.dsfbase.service.component.network.NettyClient;
import com.ly.dsfbase.service.component.network.NettyClientPool;
import com.ly.dsfbase.service.util.DateUtil;
import com.ly.dsfbase.service.util.HttpUtils;
import com.ly.dsfbase.service.util.RandomUtil;

/**
 * 图片客户端，支持图片上传、裁剪<br>
 * QQ：545526083
 * 
 * @author 李松
 * @version 1.0.0, 2016年3月11日-下午5:16:15
 * @since 2016年3月11日-下午5:16:15
 */
public class ImgClient implements InitializingBean {
	
	private static final Logger logger = LoggerFactory.getLogger(ImgClient.class);
	
	/**
	 * 图片服务配置
	 */
	private String imgConfigUrl;
	
	private String host;
	
	private int port;
	
	private int maxConns;
	
	private int initConns;
	
	/**
	 * 图片配置缓存
	 */
	private Multimap<String, String> imgConfCache = ArrayListMultimap.create();
	
	// 请求上传图片命令
	private static final String CREATE_IMG_CMD = "create_open";
	
	// 关闭上上图片命令
	private static final String CLOSE_IMG_CMD = "create_close";
	
	private static Pattern OK_PATTERN = Pattern.compile("^OK\\s+\\d*\\s*(\\S*)");

    private static final int ARGS_PART = 1;
    
    // 上传图片成功返回值
    private static final String UPLOAD_OK = "OK";
    
    //裁剪类型
    public static final String CUT_TYPE_00 = "00"; //尺寸裁剪不加水印
    public static final String CUT_TYPE_01 = "01"; //尺寸裁剪加水印
    public static final String CUT_TYPE_02 = "02"; //尺寸等比例裁剪不加水印
    public static final String CUT_TYPE_03 = "03"; //尺寸等比例裁剪加水印
    
    // 底层Socket通信连接池
    private NettyClientPool nettyClientPool;
	
    
    @Override
    public void afterPropertiesSet() throws Exception {
    	this.nettyClientPool = new NettyClientPool(host, port, maxConns, initConns);
    	this.loadImgConfig();
    }
    
    public ImgClient() {
    }
    
    public ImgClient(String host, int port, int maxConns, int initConns) {
    	this.nettyClientPool = new NettyClientPool(host, port, maxConns, initConns);
    	this.loadImgConfig();
    }
	
	/**
	 * 关闭图片客户端
	 */
	public void shutdown() {
		this.nettyClientPool.destroy();
	}
	
	/**
	 * 图片裁剪
	 * @param url  原始图片访问路径
	 * @param catalog  图片的根目录
	 * @param width  要裁剪的宽度
	 * @param height  要裁剪的高度
	 * @param cutType 裁剪类型
	 * @return
	 */
	public String cutImg(String url, String catalog, int width, int height, String cutType) {
		int index = url.lastIndexOf(".");
		String part1 = url.substring(0, index);
		String part2 = "_" + width + "x" + height + "_" + cutType;
		String part3 = url.substring(index);
		
		Collection<String> imgSizes = this.imgConfCache.get(catalog);
		if(imgSizes != null && imgSizes.contains(part2)) {
			return part1 + part2 + part3;
		} else {
			throw new RuntimeException("裁剪类型不支持!");
		}
	}
	
	/**
	 * 上传图片
	 * @param domain  图片服务器域名 如：pic3.40017.cn
	 * @param catalog 图片类别目录  如：/hotel
	 * @param imgFile
	 * @return
	 * @throws Exception
	 */
	public String uploadImg(String domain, String catalog, File imgFile) throws Exception {
		if(imgFile == null || !imgFile.isFile() || !imgFile.exists()) {
			throw new Exception("文件不存在！");
		}
		
		String timeDir = DateUtil.date2String(new Date(), "/yyyy/MM/dd/HH/");
		String picName = RandomUtil.nextString();
		String suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf("."));
		
		return this.uploadImg(domain, catalog, timeDir+picName+suffix, imgFile);
	}
	
	
//	/**
//	 * 上传图片
//	 * @param domain  图片服务器域名 如：pic3.40017.cn
//	 * @param catalog 图片类别目录  如：/hotel
//	 * @param key  图片具体路径  如：/2016/03/11/wld0022.jpg
//	 * @param imgFile 图片文件对象
//	 * @return
//	 * @throws Exception
//	 */
//	private String uploadImg(String domain, String catalog, String key, File imgFile) throws Exception {
//		NettyClient nettyClient = null;
//		
//		// 图片上传状态
//		final AtomicBoolean status = new AtomicBoolean(true);
//		
//		try {
//			//获取Socket客户端
//			nettyClient = this.nettyClientPool.borrowObject();
//			
//			final CountDownLatch createCDL = new CountDownLatch(1);
//			
//			//生成上传图片请求命令
//			String argString = encodeURLString(new String[] { 
//																"domain", catalog, 
//																"key", key, 
//																"class", "class1"
//											   });
//			String request = CREATE_IMG_CMD + " " + argString + "\r\n";
//			
//			// 返回结果
//			final Map<String, String> createRespMap = new HashMap<>();
//			
//			//发送上传图片请求消息
//			nettyClient.send(request, new SocketCallback() {
//				
//				@Override
//				public void callback(String msg) throws Exception {
//					//解析返回结果
//			        Matcher ok = OK_PATTERN.matcher(msg);
//			        if (ok.matches()) {
//			        	createRespMap.putAll(decodeURLString(ok.group(ARGS_PART)));
//			        } else {
//			        	status.set(false);
//			        	logger.error("发送上传请求失败：" + msg);
//			        }
//			        
//			        createCDL.countDown();
//				}
//			});
//			
//			//等待返回结果
//			createCDL.await(2000, TimeUnit.MILLISECONDS);
//			if(createRespMap.isEmpty()) {
//				status.set(false);
//				throw new Exception("上传请求失败");
//			}
//			
//			//上传图片文件
//			this.sendFile(createRespMap.get("path"), imgFile);
//			
//			//生成上传完成命令
//			argString = encodeURLString(new String[] {
//			        "fid", createRespMap.get("fid"), 
//			        "devid", createRespMap.get("devid"), 
//			        "domain", catalog, 
//			        "size", Long.toString(imgFile.length()), 
//			        "key", key, 
//			        "path", createRespMap.get("path") 
//			});
//			request = CLOSE_IMG_CMD + " " + argString + "\r\n";
//			
//			final CountDownLatch closeCDL = new CountDownLatch(1);
//			
//			//发送消息
//			nettyClient.send(request, new SocketCallback() {
//				
//				@Override
//				public void callback(String msg) throws Exception {
//					if(msg != null) {
//						msg = msg.trim();
//					}
//					
//					if(!UPLOAD_OK.equals(msg)) {
//						status.set(false);
//			        	logger.error("发送上传完成请求失败：" + msg);
//					}
//
//					createCDL.countDown();
//				}
//			});
//			
//			closeCDL.await(2000, TimeUnit.MILLISECONDS);
//		} catch (Exception e) {
//			logger.error("", e);
//			throw e;
//		} finally {
//			if(nettyClient != null) {
//				this.nettyClientPool.returnObject(nettyClient);
//			}
//		}
//		
//		if(!status.get()) {
//			throw new Exception("上传失败");
//		}
//		
//		return domain+catalog+key;
//	}
	
	/**
	 * 上传图片
	 * @param domain  图片服务器域名 如：pic3.40017.cn
	 * @param catalog 图片类别目录  如：/hotel
	 * @param key  图片具体路径  如：/2016/03/11/wld0022.jpg
	 * @param imgFile 图片文件对象
	 * @return
	 * @throws Exception
	 */
	private String uploadImg(String domain, String catalog, String key, File imgFile) throws Exception {
		NettyClient nettyClient = null;
		
		// 图片上传状态
		final AtomicBoolean status = new AtomicBoolean(true);
		
		try {
			//获取Socket客户端
			nettyClient = this.nettyClientPool.borrowObject();
			
			//生成上传图片请求命令
			String argString = encodeURLString(new String[] { 
																"domain", catalog, 
																"key", key, 
																"class", "class1"
											   });
			String request = CREATE_IMG_CMD + " " + argString + "\r\n";
			
			//发送上传图片请求消息
			String response = nettyClient.send(request);
			
			//解析返回结果
	        Matcher ok = OK_PATTERN.matcher(response);
	        Map<String, String> createRespMap = new HashMap<>();
	        if (ok.matches()) {
	        	createRespMap.putAll(decodeURLString(ok.group(ARGS_PART)));
	        } else {
	        	status.set(false);
	        	throw new Exception("发送上传请求失败：" + response);
	        }
	        
			if(createRespMap.isEmpty()) {
				status.set(false);
				throw new Exception("发送上传请求失败");
			}
			
			//上传图片文件
			response = this.sendFile(createRespMap.get("path"), imgFile);
			if(!UPLOAD_OK.equals(response)) {
				status.set(false);
				throw new Exception("上传图片文件失败：" + response);
			}
			
			//生成上传完成命令
			argString = encodeURLString(new String[] {
			        "fid", createRespMap.get("fid"), 
			        "devid", createRespMap.get("devid"), 
			        "domain", catalog, 
			        "size", Long.toString(imgFile.length()), 
			        "key", key, 
			        "path", createRespMap.get("path") 
			});
			request = CLOSE_IMG_CMD + " " + argString + "\r\n";
			
			//发送消息
			response = nettyClient.send(request);
			
			if(response != null) {
				response = response.trim();
			}
			
			if(!UPLOAD_OK.equals(response)) {
				status.set(false);
	        	logger.error("发送上传完成请求失败：" + response);
			}
			
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		} finally {
			if(nettyClient != null) {
				this.nettyClientPool.returnObject(nettyClient);
			}
		}
		
		if(!status.get()) {
			throw new Exception("上传失败");
		}
		
		return domain+catalog+key;
	}
	
	/**
	 * 对请求参数编码
	 * @param args
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String encodeURLString(String[] args) throws UnsupportedEncodingException {
		try {
			StringBuffer encoded = new StringBuffer();

			for (int i = 0; i < args.length; i += 2) {
				String key = args[i];
				String value = args[i + 1];

				if (encoded.length() > 0)
					encoded.append("&");
				encoded.append(key);
				encoded.append("=");
				encoded.append(URLEncoder.encode(value, "UTF-8"));
			}

			return encoded.toString();

		} catch (UnsupportedEncodingException e) {
			logger.error("编码失败", e);
			throw e;
		}
	}

	/**
	 * 对返回内容解码
	 * @param encoded
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static Map<String, String> decodeURLString(String encoded) throws UnsupportedEncodingException {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			if ((encoded == null) || (encoded.length() == 0))
				return map;

			String parts[] = encoded.split("&");
			for (int i = 0; i < parts.length; i++) {
				String pair[] = parts[i].split("=");

				if ((pair == null) || (pair.length != 2)) {
					continue;
				}

				map.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
			}

			return map;

		} catch (UnsupportedEncodingException e) {
			logger.error("解码失败", e);
			throw e;
		}
	}
	
	/**
	 * 上传文件
	 * @param url
	 * @param file
	 * @return
	 * @throws Exception 
	 */
	private String sendFile(String url, File file) throws Exception {
		String resp = null;

		FileInputStream fin = null;
		Socket socket = null;
		OutputStream out = null;
		Writer writer = null;

		try {
			socket = new Socket();
			socket.setSoTimeout(3000);
			URL parsedPath = new URL(url);
			socket.connect(new InetSocketAddress(parsedPath.getHost(),
					parsedPath.getPort()));
			out = socket.getOutputStream();

			writer = new OutputStreamWriter(out);
			writer.write("PUT ");
			writer.write(parsedPath.getPath());
			writer.write(" HTTP/1.0\r\nContent-length: ");
			writer.write(Long.toString(file.length()));
			writer.write("\r\n\r\n");
			writer.flush();

			fin = new FileInputStream(file);
			byte[] buffer = new byte[10240];
			int count = 0;
			while ((count = fin.read(buffer)) >= 0) {
				out.write(buffer, 0, count);
			}

			resp = IOUtils.toString(socket.getInputStream());
			if(resp.contains("201 Created")) {
				resp = UPLOAD_OK;
			}

		} catch (IOException e) {
			logger.error("上传图片失败", e);
			throw e;
		} finally {
			if(writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error("关闭写入流失败", e);
				}
			}
			
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("关闭socket流失败", e);
				}
			}

			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					logger.error("关闭socket失败", e);
				}
			}

			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					logger.error("关闭文件流失败", e);
				}
			}
		}
		
		return resp;
	}
	
	/**
	 * 加载图片配置信息
	 */
	@SuppressWarnings("unchecked")
	private void loadImgConfig() {
		String rs = HttpUtils.get(imgConfigUrl);
		if(!Strings.isNullOrEmpty(rs)) {
			try {
				Document document = DocumentHelper.parseText(rs);
				Element confList = document.getRootElement().element("ConfList");
				
				Iterator<Element> it = confList.elementIterator("Conf");
				while(it.hasNext()) {
					Element config = it.next();
					String pathPrefix = config.elementTextTrim("PathPrefix");
					
					Iterator<Element> sizes = config.element("ListImageConfig").elementIterator("ImageConfig");
					while(sizes.hasNext()) {
						Element imgConf = sizes.next();
						
						String size = imgConf.elementTextTrim("ScaleSize");
						String needWM = imgConf.elementTextTrim("NeedWatermark");
						
						if(("0").equals(needWM)) { //不支持水印
							this.imgConfCache.put(pathPrefix, "_" + size + "_" + CUT_TYPE_00);
							this.imgConfCache.put(pathPrefix, "_" + size + "_" + CUT_TYPE_02);
						} else { //支持加水印
							this.imgConfCache.put(pathPrefix, "_" + size + "_" + CUT_TYPE_00);
							this.imgConfCache.put(pathPrefix, "_" + size + "_" + CUT_TYPE_01);
							this.imgConfCache.put(pathPrefix, "_" + size + "_" + CUT_TYPE_02);
							this.imgConfCache.put(pathPrefix, "_" + size + "_" + CUT_TYPE_03);
						}
					}
				}
			} catch (DocumentException e) {
				logger.error("解析图片配置失败", e);
			}
		}
		
	}

	public String getImgConfigUrl() {
		return imgConfigUrl;
	}

	public void setImgConfigUrl(String imgConfigUrl) {
		this.imgConfigUrl = imgConfigUrl;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxConns() {
		return maxConns;
	}

	public void setMaxConns(int maxConns) {
		this.maxConns = maxConns;
	}

	public int getInitConns() {
		return initConns;
	}

	public void setInitConns(int initConns) {
		this.initConns = initConns;
	}
}

