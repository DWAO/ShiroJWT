package com.zorpz.zcoe.shiro.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author: wangkui
 * @Date: 2019/4/10 10:28
 * @Description: Redis配置
 */

@Configuration
@PropertySource("classpath:redis.properties")
@Log4j2
public class RedisConfig {

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWaitMillis;

    @Value("${spring.redis.block-when-exhausted}")
    private boolean  blockWhenExhausted;

    @Value("${spring.redis.expire}")
    public long expireTime;

    /**
     * @description: 配置Key的生成方式
     *
     * @param: []
     * @return: org.springframework.cache.interceptor.KeyGenerator
     * @author: wangkui
     * @date: 2019/4/11 14:49
     * @version: V1.0
     */
    /*@Bean
    public KeyGenerator keyGenerator() {

        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(o.getClass().getName())
                        .append(method.getName());
                for (Object object : objects) {
                    stringBuilder.append(object.toString());
                }
                return stringBuilder.toString();
            }
        };
    }*/
    /**
     * @description: 创建redis连接工厂
     *
     * @param: [dbIndex, host, port, password, timeout]
     * @return: redis.clients.jedis.JedisPool
     * @author: wangkui
     * @date: 2019/4/11 14:49
     * @version: V1.0
     */
    public JedisPool createJedisConnectionFactory(int dbIndex, String host, int port, String password, int timeout) {
        JedisPoolConfig poolConfig = setPoolConfig(maxIdle, minIdle, maxActive, maxWaitMillis, true);
        JedisPool jedisPool = new JedisPool(poolConfig, host, port, timeout, password,dbIndex);
        return jedisPool;

    }
    /**
     * @description: 设置连接池属性
     *
     * @param: [maxIdle, minIdle, maxActive, maxWait, testOnBorrow]
     * @return: redis.clients.jedis.JedisPoolConfig
     * @author: wangkui
     * @date: 2019/4/11 14:48
     * @version: V1.0
     */
    public JedisPoolConfig setPoolConfig(int maxIdle, int minIdle, int maxActive, int maxWait, boolean testOnBorrow) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setTestOnBorrow(testOnBorrow);
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        poolConfig.setBlockWhenExhausted(blockWhenExhausted);
        // 是否启用pool的jmx管理功能, 默认true
        poolConfig.setJmxEnabled(true);
        return poolConfig;
    }
}

