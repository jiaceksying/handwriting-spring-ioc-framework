package com.ksying.spring.factory.impl;

import com.ksying.spring.factory.BeanFactory;
import com.ksying.spring.ioc.BeanDefinition;
import com.ksying.spring.registry.impl.DefaultSingletoneBeanRegistry;

/**
 * @author <a href="jiace.ksying@gmail.com">jiakai.zhang</a>
 * @version v1.0 , 2020/4/11 16:31
 */
public abstract class AbstractBeanFactory extends DefaultSingletoneBeanRegistry implements BeanFactory {
    @Override
    public Object getBean(String beanName) {
        // 先去一级缓存中获取单例bean
        Object singletonObject = getSingleton(beanName);
        if (singletonObject != null) {
            return singletonObject;
        }
        // 一级缓存中没有，则先获取bean信息，然后通过bean信息进行创建
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        if (beanDefinition == null || beanDefinition.getClassType() == null) {
            return null;
        }

        // 判断创建单例bean还是原型bean
        if (beanDefinition.isSingleton()) {
            // 单例bean创建完成后，要塞入到一级缓存中
            singletonObject = createBean(beanDefinition);
            addSingleton(beanName, singletonObject);
        } else if (beanDefinition.isPrototype()) {
            singletonObject = createBean(beanDefinition);
        } else {
            // todo
        }
        return singletonObject;
    }

    protected abstract Object createBean(BeanDefinition beanDefinition);

    protected abstract BeanDefinition getBeanDefinition(String beanName);
}
