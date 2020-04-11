package com.ksying.spring.registry;

import com.ksying.spring.ioc.BeanDefinition;

import java.util.List;

/**
 * @author <a href="jiace.ksying@gmail.com">jiakai.zhang</a>
 * @version v1.0 , 2020/4/11 15:56
 */
public interface BeanDefinitionRegistry {
    BeanDefinition getBeanDefinition(String beanName);

    List<BeanDefinition> getBeanDefinitions();

    void registryBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
