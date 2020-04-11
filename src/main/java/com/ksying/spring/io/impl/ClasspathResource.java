package com.ksying.spring.io.impl;

import com.ksying.spring.io.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

/**
 * @author <a href="jiace.ksying@gmail.com">ksying</a>
 * @version v1.0 , 2020/4/11 16:10
 */
@Data
@AllArgsConstructor
public class ClasspathResource implements Resource {
    private String local;

    @Override
    public InputStream getResource() {
        return this.getClass().getClassLoader().getResourceAsStream(local);
    }
}
