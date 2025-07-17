package com.swxy.wfhrpc.core;

import com.swxy.wfhrpc.NettyBootstrapInitializer;
import com.swxy.wfhrpc.RpcBootstrap;
import com.swxy.wfhrpc.compress.CompressorFactory;
import com.swxy.wfhrpc.disocovery.Registry;
import com.swxy.wfhrpc.enumeration.RequestType;
import com.swxy.wfhrpc.serialize.SerializerFactory;
import com.swxy.wfhrpc.transport.message.RpcRequest;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 心跳探测的核心目的是什么？探活，感知哪些服务器的连接状态是正常的，哪些是不正常的
 *
 * @author it楠老师
 * @createTime 2023-07-07
 */
@Slf4j
public class HeartbeatDetector {
    
    public static void detectHeartbeat(String ServiceName) {
        // 1、从注册中心拉取服务列表并建立连接
        Registry registry = RpcBootstrap.getInstance().getConfiguration().getRegistryConfig().getRegistry();
        List<InetSocketAddress> addresses = registry.lookup(ServiceName,
            RpcBootstrap.getInstance().getConfiguration().getGroup()
        );
        
        // 将连接进行缓存
        for (InetSocketAddress address : addresses) {
            try {
                if (!RpcBootstrap.CHANNEL_CACHE.containsKey(address)) {
                    Channel channel = NettyBootstrapInitializer.getBootstrap().connect(address).sync().channel();
                    RpcBootstrap.CHANNEL_CACHE.put(address, channel);
                }
                
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        
        // 3、任务，定期发送消息
        Thread thread = new Thread(() ->
            new Timer().scheduleAtFixedRate(new MyTimerTask(), 0, 2000)
            , "yrpc-HeartbeatDetector-thread");
        thread.setDaemon(true);
        thread.start();
        
    }
    
    private static class MyTimerTask extends TimerTask {
        
        @Override
        public void run() {
            
            // 将响应时长的map清空
            RpcBootstrap.ANSWER_TIME_CHANNEL_CACHE.clear();
            
            // 遍历所有的channel
            Map<InetSocketAddress, Channel> cache = RpcBootstrap.CHANNEL_CACHE;
            for (Map.Entry<InetSocketAddress, Channel> entry : cache.entrySet()) {
                // 定义一个重试的次数
                int tryTimes = 3;
                while (tryTimes > 0) {
                    // 通过心跳检测处理每一个channel
                    Channel channel = entry.getValue();
                    
                    long start = System.currentTimeMillis();
                    // 构建一个心跳请求
                    RpcRequest rpcRequest = RpcRequest.builder()
                        .requestId(RpcBootstrap.getInstance().getConfiguration().getIdGenerator().getId())
                        .compressType(CompressorFactory.getCompressor(RpcBootstrap.getInstance()
                            .getConfiguration().getCompressType()).getCode())
                        .requestType(RequestType.HEART_BEAT.getId())
                        .serializeType(SerializerFactory.getSerializer(RpcBootstrap.getInstance()
                            .getConfiguration().getSerializeType()).getCode())
                        .timeStamp(start)
                        .build();
                    
                    // 4、写出报文
                    CompletableFuture<Object> completableFuture = new CompletableFuture<>();
                    // 将 completableFuture 暴露出去
                    RpcBootstrap.PENDING_REQUEST.put(rpcRequest.getRequestId(), completableFuture);
                    
                    channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) promise -> {
                        if (!promise.isSuccess()) {
                            completableFuture.completeExceptionally(promise.cause());
                        }
                    });
                    
                    Long endTime = 0L;
                    try {
                        // 阻塞方法，get()方法如果得不到结果，就会一直阻塞
                        // 我们想不一直阻塞可以添加参数
                        completableFuture.get(1, TimeUnit.SECONDS);
                        endTime = System.currentTimeMillis();
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        // 一旦发生问题，需要优先重试
                        tryTimes --;
                        log.error("和地址为【{}】的主机连接发生异常.正在进行第【{}】次重试......",
                            channel.remoteAddress(), 3 - tryTimes);
                        
                        // 将重试的机会用尽，将失效的地址移出服务列表
                        if(tryTimes == 0){
                            RpcBootstrap.CHANNEL_CACHE.remove(entry.getKey());
                        }
                        
                        // 尝试等到一段时间后重试
                        try {
                            Thread.sleep(10*(new Random().nextInt(5)));
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
    
                        continue;
                    }
                    Long time = endTime - start;
                    
                    // 使用treemap进行缓存
                    RpcBootstrap.ANSWER_TIME_CHANNEL_CACHE.put(time, channel);
                    log.debug("和[{}]服务器的响应时间是[{}].", entry.getKey(), time);
                    break;
                }
            }
            
            log.info("-----------------------响应时间的treemap----------------------");
            for (Map.Entry<Long, Channel> entry : RpcBootstrap.ANSWER_TIME_CHANNEL_CACHE.entrySet()) {
                if (log.isDebugEnabled()) {
                    log.debug("[{}]--->channelId:[{}]", entry.getKey(), entry.getValue().id());
                }
            }
        }
    }
    
    /**
     * Provider端：定时向Redis上报心跳（支持密码）
     */
    public static void reportHeartbeatToRedis(String serviceName, String ip, int port, String redisHost, int redisPort, String redisPassword) {
        new Thread(() -> {
            Jedis jedis = new Jedis(redisHost, redisPort);
            if (redisPassword != null && !redisPassword.isEmpty()) {
                jedis.auth(redisPassword);
            }
            String key = "yrpc:heartbeat:" + serviceName + ":" + ip + ":" + port;
            while (true) {
                try {
                    jedis.setex(key, 15, String.valueOf(System.currentTimeMillis())); // 15秒过期
                    Thread.sleep(5000); // 5秒上报一次
                } catch (Exception e) {
                    log.error("Redis心跳上报异常", e);
                }
            }
        }, "yrpc-redis-heartbeat-provider").start();
    }

    /**
     * Consumer端：定时从Redis检测所有服务节点心跳
     */
    public static void checkHeartbeatFromRedis(String serviceName, String redisHost, int redisPort, long timeoutMs) {
        new Thread(() -> {
            Jedis jedis = new Jedis(redisHost, redisPort);
            while (true) {
                try {
                    Set<String> keys = jedis.keys("yrpc:heartbeat:" + serviceName + ":*");
                    long now = System.currentTimeMillis();
                    for (String key : keys) {
                        String value = jedis.get(key);
                        if (value == null) continue;
                        long last = Long.parseLong(value);
                        if (now - last < timeoutMs) {
                            log.info("节点存活: {}", key);
                        } else {
                            log.warn("节点失效: {}", key);
                        }
                    }
                    Thread.sleep(5000); // 5秒检测一次
                } catch (Exception e) {
                    log.error("Redis心跳检测异常", e);
                }
            }
        }, "yrpc-redis-heartbeat-consumer").start();
    }
}
