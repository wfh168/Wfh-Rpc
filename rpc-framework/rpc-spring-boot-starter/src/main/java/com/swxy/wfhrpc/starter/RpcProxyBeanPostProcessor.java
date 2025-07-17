package com.swxy.wfhrpc.starter;

import com.swxy.wfhrpc.annotation.RpcService;
import com.swxy.wfhrpc.proxy.RpcProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * Spring Boot Starter自动装配的Consumer端代理注入处理器
 */
public class RpcProxyBeanPostProcessor implements BeanPostProcessor {
    private final RpcProperties properties;
    public RpcProxyBeanPostProcessor(RpcProperties properties) {
        this.properties = properties;
    }
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            RpcService rpcService = field.getAnnotation(RpcService.class);
            if(rpcService != null){
                Class<?> type = field.getType();
                Object proxy = RpcProxyFactory.getProxy(type, properties);
                field.setAccessible(true);
                try {
                    field.set(bean,proxy);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }
} 