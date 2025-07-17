package com.swxy.wfhrpc.proxy;


import com.swxy.wfhrpc.ReferenceConfig;
import com.swxy.wfhrpc.RpcBootstrap;
import com.swxy.wfhrpc.disocovery.RegistryConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wfh168
 * @createTime 2023-07-30
 */
public class RpcProxyFactory {
    
    private static Map<Class<?>,Object> cache = new ConcurrentHashMap<>(32);
    
    public static <T> T getProxy(Class<T> clazz) {
    
        Object bean = cache.get(clazz);
        if(bean != null){
            return (T)bean;
        }
    
        ReferenceConfig<T> reference = new ReferenceConfig<>();
        reference.setInterface(clazz);
        
        // 代理做了些什么?
        // 1、连接注册中心
        // 2、拉取服务列表
        // 3、选择一个服务并建立连接
        // 4、发送请求，携带一些信息（接口名，参数列表，方法的名字），获得结果
        RpcBootstrap.getInstance()
            .application("first-rpc-consumer")
            .registry(new RegistryConfig("zookeeper://127.0.0.1:2181"))
            .serialize("hessian")
            .compress("gzip")
            .group("primary")
            .reference(reference);
        T t = reference.get();
        cache.put(clazz,t);
        return t;
    }

    public static <T> T getProxy(Class<T> clazz, Object properties) {
        // 兼容starter注入配置
        Object bean = cache.get(clazz);
        if(bean != null){
            return (T)bean;
        }
        ReferenceConfig<T> reference = new ReferenceConfig<>();
        reference.setInterface(clazz);
        if (properties != null && properties.getClass().getSimpleName().equals("RpcProperties")) {
            try {
                String application = (String) properties.getClass().getMethod("getApplication").invoke(properties);
                String registry = (String) properties.getClass().getMethod("getRegistry").invoke(properties);
                String serialize = (String) properties.getClass().getMethod("getSerialize").invoke(properties);
                String compress = (String) properties.getClass().getMethod("getCompress").invoke(properties);
                String group = (String) properties.getClass().getMethod("getGroup").invoke(properties);
                RpcBootstrap.getInstance()
                    .application(application)
                    .registry(new RegistryConfig(registry))
                    .serialize(serialize)
                    .compress(compress)
                    .group(group)
                    .reference(reference);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // fallback
            return getProxy(clazz);
        }
        T t = reference.get();
        cache.put(clazz,t);
        return t;
    }
}
