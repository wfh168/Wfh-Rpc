package com.swxy.wfhrpc.starter;

import com.swxy.wfhrpc.core.HeartbeatDetector;
import com.swxy.wfhrpc.utils.NetUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RpcProperties.class)
public class RpcAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RpcStarter yrpcStarter(RpcProperties properties) {
        // Provider端自动启动
        if (properties.isEnableRedisHeartbeat()) {
            String ip = NetUtils.getIp();
            String redisHost = properties.getRedisHost();
            int redisPort = properties.getRedisPort();
            String redisPassword = properties.getRedisPassword();
            // 支持集群配置
            String[] clusterNodes = properties.getRedisClusterNodes();
            if (clusterNodes != null && clusterNodes.length > 0) {
                // TODO: 集群模式下可用JedisCluster
                // 这里只做演示，实际可扩展
            }
            HeartbeatDetector.reportHeartbeatToRedis(
                properties.getApplication(),
                ip,
                properties.getPort() > 0 ? properties.getPort() : 8081,
                redisHost,
                redisPort,
                redisPassword
            );
        }
        return new RpcStarter(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcProxyBeanPostProcessor yrpcProxyBeanPostProcessor(RpcProperties properties) {
        // Consumer端自动检测心跳
        if (properties.isEnableRedisHeartbeat()) {
            HeartbeatDetector.checkHeartbeatFromRedis(
                properties.getApplication(),
                properties.getRedisHost(),
                properties.getRedisPort(),
                properties.getHeartbeatTimeoutMs()
            );
        }
        return new RpcProxyBeanPostProcessor(properties);
    }
} 