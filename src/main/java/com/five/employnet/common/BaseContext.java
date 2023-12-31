package com.five.employnet.common;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

public class BaseContext {

    static private final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    static public String getCurrentId() {
        return threadLocal.get();
    }

    static public void setCurrentId(String id) {
        threadLocal.set(id);
    }

    static public void removeCurrentId() {
        threadLocal.remove();
    }

    static public AnnotationConfigApplicationContext getApplicationContext() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        // 添加一个属性源（application.yml）
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        Properties properties = yaml.getObject();
        PropertySource<?> propertySource = new PropertiesPropertySource("customPropertySource", properties);
        ConfigurableEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(propertySource);
        applicationContext.setEnvironment(environment);

        // 环境实例初始化
        applicationContext.refresh();
        return applicationContext;
    }
}
