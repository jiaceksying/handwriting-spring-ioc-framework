package com.ksying.spring.registry.impl;

import com.ksying.spring.registry.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="jiace.ksying@gmail.com">jiakai.zhang</a>
 * @version v1.0 , 2020/4/11 16:36
 */
public class DefaultSingletoneBeanRegistry implements SingletonBeanRegistry {
    // 单例bean缓存，一级缓存
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }

    @Override
    public void addSingleton(String beanName, Object bean) {
        this.singletonObjects.put(beanName, bean);
    }
}
