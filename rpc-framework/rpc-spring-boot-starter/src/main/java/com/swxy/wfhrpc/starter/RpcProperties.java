package com.swxy.wfhrpc.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rpc")
public class RpcProperties {
    /** 应用名 */
    private String application;
    /** 注册中心地址 */
    private String registry;
    /** 序列化方式 */
    private String serialize = "jdk";
    /** 压缩方式 */
    private String compress = "gzip";
    /** 服务扫描包 */
    private String scan;
    /** 分组 */
    private String group = "primary";
    /** Redis主机 */
    private String redisHost = "localhost";
    /** Redis端口 */
    private int redisPort = 6379;
    /** 是否启用Redis心跳机制 */
    private boolean enableRedisHeartbeat = false;
    /** 心跳超时时间（毫秒） */
    private long heartbeatTimeoutMs = 10000;
    /** Redis密码 */
    private String redisPassword;
    /** Redis集群节点（逗号分隔） */
    private String[] redisClusterNodes;
    /** 服务端口 */
    private int port = 8081;
    // ... 其他字段 ...



    // getter/setter
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public String getApplication() {
        return application;
    }
    public void setApplication(String application) { this.application = application; }
    public String getRegistry() { return registry; }
    public void setRegistry(String registry) { this.registry = registry; }
    public String getSerialize() { return serialize; }
    public void setSerialize(String serialize) { this.serialize = serialize; }
    public String getCompress() { return compress; }
    public void setCompress(String compress) { this.compress = compress; }
    public String getScan() { return scan; }
    public void setScan(String scan) { this.scan = scan; }
    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }
    public String getRedisHost() { return redisHost; }
    public void setRedisHost(String redisHost) { this.redisHost = redisHost; }
    public int getRedisPort() { return redisPort; }
    public void setRedisPort(int redisPort) { this.redisPort = redisPort; }
    public boolean isEnableRedisHeartbeat() { return enableRedisHeartbeat; }
    public void setEnableRedisHeartbeat(boolean enableRedisHeartbeat) { this.enableRedisHeartbeat = enableRedisHeartbeat; }
    public long getHeartbeatTimeoutMs() { return heartbeatTimeoutMs; }
    public void setHeartbeatTimeoutMs(long heartbeatTimeoutMs) { this.heartbeatTimeoutMs = heartbeatTimeoutMs; }
    public String getRedisPassword() { return redisPassword; }
    public void setRedisPassword(String redisPassword) { this.redisPassword = redisPassword; }
    public String[] getRedisClusterNodes() { return redisClusterNodes; }
    public void setRedisClusterNodes(String[] redisClusterNodes) { this.redisClusterNodes = redisClusterNodes; }
} 