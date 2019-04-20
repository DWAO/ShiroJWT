package com.zorpz.zcoe.shiro.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;

/**
 * @Author: wangkui
 * @Date: 2019/4/10 10:28
 * @Description: Redis配置
 */

@Configuration
@PropertySource("classpath:redis.properties")
@Log4j2
public class DefaultRedisConfig extends RedisConfig{

    @Value("${spring.redis.database}")
    private int dbIndex;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public JedisPool defaultRedisConnectionFactory() {
        log.info("JedisPool注入成功！！");
        log.info("redis地址：" + host + ":" + port);
        return createJedisConnectionFactory(dbIndex, host, port, password, timeout);
    }
}

