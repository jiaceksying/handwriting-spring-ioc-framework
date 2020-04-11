package com.ksying.spring.reader;

import com.ksying.spring.ioc.BeanDefinition;
import com.ksying.spring.ioc.PropertyValue;
import com.ksying.spring.ioc.RuntimeBeanReference;
import com.ksying.spring.ioc.TypedStringValue;
import com.ksying.spring.registry.BeanDefinitionRegistry;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author <a href="jiace.ksying@gmail.com">jiakai.zhang</a>
 * @version v1.0 , 2020/4/11 16:15
 */
@AllArgsConstructor
public class XmlBeanDefinitionDocumentReader {
    private BeanDefinitionRegistry beanDefinitionRegistry;

    public void registryBeanDefinitions(Element rootElement) {
        List<Element> elements = rootElement.elements("bean");
        elements.forEach(
                element -> {
                    if ("bean".equals(element.getName())) {
                        parseDefaultElement(element);
                    } else {
                        parseCustomElement(element);
                    }
                }
        );
    }

    private void parseDefaultElement(Element element) {
        try {
            if (element == null) {
                return;
            }
            String id = element.attributeValue("id");
            String name = element.attributeValue("name");
            String className = element.attributeValue("class");
            if (StringUtils.isBlank(className)) {
                return;
            }
            String initMethod = element.attributeValue("init-method");
            String scope = StringUtils.isBlank(element.attributeValue("scope")) ? "singleton" : element.attributeValue("scope");
            String beanName = StringUtils.isBlank(id) ? name : id;
            Class classType = Class.forName(className);
            beanName = StringUtils.isBlank(beanName) ? classType.getSimpleName() : beanName;
            BeanDefinition beanDefinition = new BeanDefinition(className, beanName);
            beanDefinition.setBeanName(beanName);
            beanDefinition.setClassName(className);
            beanDefinition.setClassType(classType);
            beanDefinition.setInitMethod(initMethod);
            beanDefinition.setScope(scope);
            List<Element> propertyElements = element.elements();
            propertyElements.forEach(
                    propertyElement -> {
                        parsePropertyElement(propertyElement, beanDefinition);
                    }
            );
            this.beanDefinitionRegistry.registryBeanDefinition(beanName, beanDefinition);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parsePropertyElement(Element propertyElement, BeanDefinition beanDefinition) {
        if (propertyElement == null) {
            return;
        }
        String name = propertyElement.attributeValue("name");
        String value = propertyElement.attributeValue("value");
        String ref = propertyElement.attributeValue("ref");
        if (StringUtils.isNotBlank(value) && StringUtils.isNotBlank(ref)) {
            return;
        }

        PropertyValue propertyValue = null;
        if (StringUtils.isNotBlank(value)) {
            TypedStringValue typedStringValue = new TypedStringValue();
            Class targetType = parseTypedStringValue(beanDefinition.getClassType(), name);
            typedStringValue.setValue(value);
            typedStringValue.setType(targetType);
            propertyValue = new PropertyValue(name, typedStringValue);
        } else if (StringUtils.isNotBlank(ref)) {
            RuntimeBeanReference runtimeBeanReference = new RuntimeBeanReference();
            runtimeBeanReference.setRef(ref);
            propertyValue = new PropertyValue(name, runtimeBeanReference);
        } else {
            return;
        }
        beanDefinition.addPropertyValues(propertyValue);
    }

    private Class parseTypedStringValue(Class classType, String name) {
        try {
            Field field = classType.getDeclaredField(name);
            return field.getType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseCustomElement(Element element) {
        
    }

}
