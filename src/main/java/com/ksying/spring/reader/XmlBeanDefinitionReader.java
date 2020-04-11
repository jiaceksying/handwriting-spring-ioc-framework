package com.ksying.spring.reader;

import com.ksying.spring.factory.impl.DefaultListableBeanFactory;
import com.ksying.spring.registry.BeanDefinitionRegistry;
import com.ksying.spring.utils.DocumentReader;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.dom4j.Document;

import java.io.InputStream;

/**
 * @author <a href="jiace.ksying@gmail.com">jiakai.zhang</a>
 * @version v1.0 , 2020/4/11 16:06
 */
@Data
@AllArgsConstructor
public class XmlBeanDefinitionReader {
    // 依赖倒置原则，依赖使用高层次抽象类或接口来进行操作
    private BeanDefinitionRegistry beanDefinitionRegistry;

    public void loadBeanDefinitions(InputStream inputStream) {
        Document document = DocumentReader.createDocument(inputStream);
        XmlBeanDefinitionDocumentReader documentReader = new XmlBeanDefinitionDocumentReader(beanDefinitionRegistry);
        documentReader.registryBeanDefinitions(document.getRootElement());
    }

}
