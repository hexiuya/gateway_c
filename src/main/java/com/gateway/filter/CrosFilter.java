package com.gateway.filter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.netflix.zuul.ZuulFilter;

public class CrosFilter extends ZuulFilter {
	 @Override  
	 public Object run() {  
		 final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        final CorsConfiguration config = new CorsConfiguration();
	        config.setAllowCredentials(true); // 允许cookies跨域
	        config.addAllowedOrigin("*");// 允许向该服务器提交请求的URI�?*表示全部允许。�?�这里尽量限制来源域，比如http://xxxx:8080 ,以降低安全风险�?��??
	        config.addAllowedHeader("*");// 允许访问的头信息,*表示全部
	        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检�?
	        config.addAllowedMethod("*");// 允许提交请求的方法，*表示全部允许，也可以单独设置GET、PUT�?
	    /*    config.addAllowedMethod("HEAD");
	        config.addAllowedMethod("GET");// 允许Get的请求方�?
	        config.addAllowedMethod("PUT");
	        config.addAllowedMethod("POST");
	        config.addAllowedMethod("DELETE");
	        config.addAllowedMethod("PATCH");*/
	        source.registerCorsConfiguration("/**", config);
	        return new CorsFilter(source);
	    }  
	  
	    @Override  
	    public boolean shouldFilter() {  
	        return true;  
	    }  
	  
	    @Override  
	    public int filterOrder() {  
	        return 0;  
	    }  
	  
	    @Override  
	    public String filterType() {  
	        return "post";// 在请求被处理之后，会进入该过滤器  
	    }  
}
