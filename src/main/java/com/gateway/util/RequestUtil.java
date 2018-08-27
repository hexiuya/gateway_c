package com.gateway.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gateway.filter.AccessFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;

public class RequestUtil {
	private static Logger log = LoggerFactory.getLogger(RequestUtil.class);
	
	public static void addClientId(RequestContext ctx , String customerDetail) throws Exception {
		try {
			Map map = JSON.parseObject(customerDetail);
			String clientidCache = String.valueOf(map.get("clientid")) ;
			
			InputStream in = ctx.getRequest().getInputStream();
			String body = StreamUtils
					.copyToString(in, Charset.forName("UTF-8"));
			System.out.println("body:" + body);
			JSONObject json = JSONObject.parseObject(body);
			String clientidParam = String.valueOf(json.get("clientid")) ;
			if (!clientidCache.equals(clientidParam)){
				throw new Exception("clientid is illegal");
			}
			
			json.put("clientid", clientidCache);
//			json.put("poid", id);
			String newBody = json.toString();
			System.out.println("newBody:" + newBody);
			final byte[] reqBodyBytes = newBody.getBytes();
			HttpServletRequest request = ctx.getRequest();
			ctx.setRequest(new HttpServletRequestWrapper(request) {
				@Override
				public ServletInputStream getInputStream() throws IOException {
					return new ServletInputStreamWrapper(reqBodyBytes);
				}

				@Override
				public int getContentLength() {
					return reqBodyBytes.length;
				}

				@Override
				public long getContentLengthLong() {
					return reqBodyBytes.length;
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public static boolean checkCustomerStatus(RequestContext ctx , String customerDetail){
		Map map = JSON.parseObject(customerDetail);
		String clientidCache = String.valueOf(map.get("clientid")) ;
		
		try {
			InputStream in = ctx.getRequest().getInputStream();
			String body = StreamUtils
					.copyToString(in, Charset.forName("UTF-8"));
			System.out.println("body:" + body);
			JSONObject json = JSONObject.parseObject(body);
			String clientidParam = String.valueOf(json.get("clientid")) ;
			if (clientidCache.equals(clientidParam)){
				return true;
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return false;
		}
		
		return false;
	}
}
