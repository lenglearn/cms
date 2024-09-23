package com.briup.cms.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 封装redis的操作
 * 提供一个静态的方法调用方法
 */
@Component
public class RedisUtil implements ApplicationContextAware {
    public static RedisTemplate redisTemplate;

    public static void putAll(String key, Map map) {
        HashOperations ops = redisTemplate.opsForHash();
        ops.putAll(key, map);


    }

    public static Map entries(String key) {
        HashOperations ops = redisTemplate.opsForHash();
        return ops.entries(key);
    }

    public static Long increment(String key, Object hashKey, long delta) {
        HashOperations ops = redisTemplate.opsForHash();
        return ops.increment(key, hashKey, delta);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        redisTemplate = (RedisTemplate) applicationContext.getBean("redisTemplate");
    }
}