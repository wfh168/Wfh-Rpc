package com.swxy.wfhrpc.starter;

import com.swxy.wfhrpc.RpcBootstrap;
import com.swxy.wfhrpc.disocovery.RegistryConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

/**
 * Spring Boot Starter自动装配的Provider端启动器
 */
@Slf4j
public class RpcStarter implements CommandLineRunner {
    private final RpcProperties properties;
    public RpcStarter(RpcProperties properties) {
        this.properties = properties;
    }
    @Override
    public void run(String... args) throws Exception {
        log.info("rpc 开始启动...");
        RpcBootstrap.getInstance()
            .application(properties.getApplication())
            .registry(new RegistryConfig(properties.getRegistry()))
            .serialize(properties.getSerialize())
            .compress(properties.getCompress())
            .scan(properties.getScan())
            .start();
    }
} 