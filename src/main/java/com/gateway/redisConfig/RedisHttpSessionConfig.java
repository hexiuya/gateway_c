package com.gateway.redisConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;


@Configuration
//maxInactiveIntervalInSeconds session超时时间,单位�?
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = httpSessionTimeOut)
@EnableRedisHttpSession
public class RedisHttpSessionConfig extends RedisHttpSessionConfiguration {
	
	@Value("${httpSessionTimeOut}")
	private Integer httpSessionTimeOut;
	
	private Integer maxInactiveIntervalInSeconds = httpSessionTimeOut;
	
	public void setMaxInactiveIntervalInSeconds(int maxInactiveIntervalInSeconds) {
		this.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds;
	}
	
	@Bean
	public RedisOperationsSessionRepository sessionRepository(RedisTemplate<String, ExpiringSession> sessionRedisTemplate) {
		RedisOperationsSessionRepository sessionRepository = new RedisOperationsSessionRepository(sessionRedisTemplate);
		sessionRepository.setDefaultMaxInactiveInterval(httpSessionTimeOut);
		return sessionRepository;
	}
}
