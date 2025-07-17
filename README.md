# Wfh-Rpc

Wfh-Rpc 是一个基于 Netty 和 ZooKeeper 的高性能、可扩展的 Java RPC（远程过程调用）框架，支持原生与 Spring Boot 两种集成方式，适合分布式服务注册、发现与调用场景。

---

## ✨ 主要特性
- **高性能通信**：底层基于 Netty 实现高效网络通信。
- **服务注册与发现**：集成 ZooKeeper 实现服务注册、发现与动态上下线。
- **Redis 心跳机制**：可选用 Redis 实现服务节点健康探测与高可用监控。
- **多种序列化支持**：支持 fastjson2、hessian 等多种序列化协议。
- **Spring Boot 友好**：提供 Starter，开箱即用。
- **模块化设计**：核心、通用、Demo、管理端分层清晰，便于扩展和维护。
- **丰富示例**：原生与 Spring Boot 消费者/服务端 Demo，快速上手。

---

## 🚀 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/wfh168/Wfh-Rpc.git
cd Wfh-Rpc
```

### 2. 构建所有模块
```bash
mvn clean install -U
```

### 3. 启动 ZooKeeper
请确保本地或远程已启动 ZooKeeper 服务，默认端口 2181。

### 4. （可选）启用 Redis 心跳机制
如需服务健康监控与高可用探测，需启动 Redis，并在 `application.properties` 配置：
```properties
rpc.enable-redis-heartbeat=true
rpc.redis-host=127.0.0.1
rpc.redis-port=6379
rpc.redis-password=yourpassword # 如无密码可省略
rpc.heartbeat-timeout-ms=10000 # 心跳超时时间（毫秒）
```

### 5. 运行示例
- **原生 Demo**：
  - `rpc-demo/rpc-provider-demo` 启动服务端
  - `rpc-demo/rpc-consumer-demo` 启动客户端
- **Spring Boot Demo**：
  - `rpc-demo/rpc-springboot-provider` 启动服务端
  - `rpc-demo/rpc-springboot-consumer` 启动客户端

进入对应模块目录，执行：
```bash
mvn spring-boot:run
```
或
```bash
java -jar target/*.jar
```

### 6. 运行测试
```bash
mvn test
```

---

## 🛠️ 主要依赖
- Netty 4.1.x
- ZooKeeper 3.8.x
- Redis（可选，心跳监控）
- JUnit 4.13.x
- Lombok 1.18.x
- Logback 1.4.x
- Commons Lang3 3.12.x
- Fastjson2 2.0.x
- Hessian 4.0.x

所有依赖版本由父工程统一管理，详见 `pom.xml`。

---

## 💡 Redis 心跳机制说明
- **服务端**：定时向 Redis 上报自身心跳（`rpc:heartbeat:服务名:ip:port`），用于服务健康状态的记录。
- **客户端**：定时从 Redis 检查所有服务节点的心跳，判断节点是否存活。
- **配置**：通过 `application.properties` 灵活启用/关闭 Redis 心跳、配置 Redis 地址、端口、密码等。
- **适用场景**：适合对服务高可用、健康监控有需求的分布式系统。

---

## 📚 贡献与交流
- 欢迎 Issue、PR 及建议！
- 作者：wfh168
- 邮箱：<wu2740461899@163.com>

---

## 📄 License
本项目仅供学习与交流，禁止用于商业用途。
