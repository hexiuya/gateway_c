package com.gateway.filter;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSONObject;
import com.gateway.constant.Constant;
import com.gateway.util.RequestUtil;
import com.gateway.util.ResponseUtil;
import com.gateway.util.StringUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class AccessFilter extends ZuulFilter  {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	@Value("${httpSessionTimeOut}")
	private int httpSessionTimeOut;
	
    private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        
        String sessionId = request.getSession().getId();

        log.info("sessionId:"+sessionId);

        HttpServletResponse response = ctx.getResponse();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        //获取请求资源路径  
        String sevletPath = request.getServletPath();
        log.info("sevletPath:" + sevletPath);
        
        //url白名单
        if(Constant.ALLOW_URL.contains(sevletPath)){
        	return null;
        }
        //登出方法
        if (Constant.LOGOUT_URL.equals(sevletPath)){
        	stringRedisTemplate.delete(sessionId);//删除自置的redis缓存
        	ctx.getRequest().getSession().invalidate();//删除自带的redis缓存，后期将这两个改成一个
        	ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(200);
            ctx.setResponseBody("{\"status\":\"SUCCESS\"}");
            ctx.getResponse().setContentType("application/json;charset=UTF-8");
            return null;
        }
        
        String body = stringRedisTemplate.opsForValue().get(sessionId);
        
        //查看个人信息url
        if(Constant.getUserInfo.equals(sevletPath)){//查看个人信息
        	ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(200);
            ctx.setResponseBody(body);
            ctx.getResponse().setContentType("application/json;charset=UTF-8");
            return null;
        }
        
        String routeUrl = StringUtil.getRouteStr(sevletPath);
        

        
//         obj =  redisTemplate.opsForValue().get(sessionId);
        
        //登录校验
        if(null == body ){
        	ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
//            String json = JSONObject.toJSON(ResponseUtil.setResult("9999", "please login")).toString();
//            ctx.setResponseBody(json);
            ctx.setResponseBody("{\"status\":\"PLEASE_LOGIN\"}");
            ctx.getResponse().setContentType("application/json;charset=UTF-8");
            return null;
        }
        
        //检查客户状态 true:已登录，false:未登录
        boolean flag = RequestUtil.checkCustomerStatus(ctx, body);
        if(!flag){
        	ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            ctx.setResponseBody("{\"status\":\"PLEASE_LOGIN\"}");
            ctx.getResponse().setContentType("application/json;charset=UTF-8");
            return null;
        }
        
        //需要加clientid的url,这样的url请求参数是json格式
        if(Constant.ADD_CLIENTID_URL.contains(routeUrl)){
        	try {
				RequestUtil.addClientId(ctx,body);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				ctx.setSendZuulResponse(false);
	            ctx.setResponseStatusCode(401);
//	            String json = JSONObject.toJSON(ResponseUtil.setResult("9999", "please login")).toString();
	            ctx.setResponseBody("{\"status\":\"PLEASE_LOGIN\"}");
	            ctx.getResponse().setContentType("application/json;charset=UTF-8");
	            return null;
			}
        }
        
        stringRedisTemplate.opsForValue().set(sessionId, body ,httpSessionTimeOut, TimeUnit.SECONDS);
        
        return null;
    }

}
