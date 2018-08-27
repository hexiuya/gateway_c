package com.gateway;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.gateway.filter.AccessFilter;
import com.gateway.filter.AfterLoginFilter;
import com.gateway.filter.CrosFilter;

@EnableZuulProxy
@SpringCloudApplication
public class Application {

	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).web(true).run(args);
	}

	
	@Bean
	public AccessFilter accessFilter() {
		return new AccessFilter();
	}
	
	@Bean
	public CrosFilter crosFilter(){
		return new CrosFilter();
	}
	
	@Bean
	public AfterLoginFilter afterLoginFilter(){
		return new AfterLoginFilter();
	}
	
}
