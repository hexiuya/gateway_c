package com.gateway.redisConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;



@Configuration
public class RedisConfig {
	@Value("${spring.redis.host}")  
    private String hostName;  
    @Value("${spring.redis.port}")  
    private int port;  
    @Value("${spring.redis.password}")  
    private String passWord;  
    @Value("${spring.redis.pool.max-idle}")  
    private int maxIdl;  
    @Value("${spring.redis.pool.min-idle}")  
    private int minIdl;  
    @Value("${spring.redis.database}")  
    private int database;  
    @Value("${spring.redis.timeout}")  
    private int timeout;  

//    @Bean
//    JedisConnectionFactory jedisConnectionFactory() {
//        return new JedisConnectionFactory();//总是连接本地的redis，要使用自定义的JedisConnectionFactory
//    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new RedisObjectSerializer());
        return template;
    }
    
    
    @Bean  
    public RedisConnectionFactory jedisConnectionFactory(){  
        JedisPoolConfig poolConfig=new JedisPoolConfig();  
        poolConfig.setMaxIdle(maxIdl);  
        poolConfig.setMinIdle(minIdl);  
        poolConfig.setTestOnBorrow(true);  
        poolConfig.setTestOnReturn(true);  
        poolConfig.setTestWhileIdle(true);  
        poolConfig.setNumTestsPerEvictionRun(10);  
        poolConfig.setTimeBetweenEvictionRunsMillis(60000);  
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);  
        jedisConnectionFactory.setHostName(hostName);   
        if(!passWord.isEmpty()){   
            jedisConnectionFactory.setPassword(passWord);   
        }   
        jedisConnectionFactory.setPort(port);   
        jedisConnectionFactory.setDatabase(database);  
        jedisConnectionFactory.setTimeout(timeout);
        return jedisConnectionFactory;  
    } 
    

}