package com.ksying.spring.factory;

/**
 * @author <a href="jiace.ksying@gmail.com">jiakai.zhang</a>
 * @version v1.0 , 2020/4/11 16:25
 */
public interface BeanFactory {
    Object getBean(String beanName);
}
