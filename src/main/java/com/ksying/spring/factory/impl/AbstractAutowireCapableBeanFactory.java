package com.ksying.spring.factory.impl;

import com.ksying.spring.ioc.BeanDefinition;
import com.ksying.spring.ioc.PropertyValue;
import com.ksying.spring.ioc.RuntimeBeanReference;
import com.ksying.spring.ioc.TypedStringValue;
import com.ksying.spring.utils.ReflectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author <a href="jiace.ksying@gmail.com">jiakai.zhang</a>
 * @version v1.0 , 2020/4/11 16:51
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {
    @Override
    protected Object createBean(BeanDefinition beanDefinition) {
        Class classType = beanDefinition.getClassType();
        if (classType == null) {
            return null;
        }
        // 创建Bean的步骤
        // 1. Bean对象实例化
//        Object bean = createInstance(classType);
        Object bean = createInstance(classType);
        // 2. Bean 对象属性赋值
        populateBean(bean, beanDefinition);

        // 3. 执行初始化方法
        initMethod(bean, beanDefinition);
        return bean;
    }

    private void initMethod(Object bean, BeanDefinition beanDefinition) {
        try {
            String initMethod = beanDefinition.getInitMethod();
            if (StringUtils.isBlank(initMethod)) {
                return;
            }
            ReflectUtils.invokeMethod(bean, initMethod);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateBean(Object bean, BeanDefinition beanDefinition) {
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
        propertyValues.forEach(
                propertyValue -> {
                    String name = propertyValue.getName();
                    Object value = propertyValue.getValue();
                    Object valueToUse = null;
                    if (value instanceof TypedStringValue) {
                        TypedStringValue typedStringValue = (TypedStringValue) value;
                        String stringValue = typedStringValue.getValue();
                        Class type = typedStringValue.getType();
                        // todo 此处可以使用策略模式优化
                        if (type.equals(Integer.class)) {
                            valueToUse = Integer.parseInt(stringValue);
                        } else if (type.equals(String.class)) {
                            valueToUse = stringValue;
                        } else {
                            // todo 其他类型。。。
                        }
                    } else if (value instanceof RuntimeBeanReference) {
                        RuntimeBeanReference reference = (RuntimeBeanReference) value;
                        String ref = reference.getRef();
                        valueToUse = getBean(ref);
                    }
                    ReflectUtils.setProperty(bean, name, valueToUse);
                }
        );
    }

    private Object createInstance(Class classType) {
        // TODO 通过实例工厂方式去创建Bean实例，比如通过factory-bean标签属性指的FactoryBean工厂去创建实例
        // TODO 通过静态工厂方法方式去创建Bean实例，比如通过factory-method标签属性指的静态工厂方法去创建实例

        // 构造方法去创建Bean实例（此处我们只针对无参构造进行操作）
        Object bean = ReflectUtils.createObject(classType);
        return bean;
    }
}
