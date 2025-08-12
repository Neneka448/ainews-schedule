# AI News Runner - Multi-Module Project

这是一个基于Spring Cloud Alibaba的多模块微服务项目，用于AI新闻处理和调度。

## 项目结构

```
ainews-schedule/
├── pom.xml                    # 父POM - 多模块根配置
└── ainews-runr/              # 调度器模块
    ├── pom.xml               # 子模块POM
    ├── src/                  # 源代码
    └── target/               # 构建输出
```

## 技术栈

### 核心框架
- **Spring Boot 3.3.0** - 应用框架
- **Spring Cloud 2022.0.4** - 微服务框架
- **Spring Cloud Alibaba 2022.0.0.0** - 阿里云微服务套件

### 微服务组件
- **Nacos** - 服务发现与配置中心
- **Kafka** - 消息队列
- **OpenFeign** - HTTP客户端
- **Sentinel** - 熔断器和限流

### 监控和日志
- **Prometheus** - 指标监控
- **Micrometer** - 指标收集
- **Zipkin** - 分布式链路追踪
- **Loki** - 日志聚合

## 基础设施依赖

项目需要以下Docker服务运行：

```bash
# Nacos (服务发现和配置中心)
docker run -d --name nacos -p 8848:8848 nacos/nacos-server

# Kafka (消息队列)
docker run -d --name kafka -p 9092:9092 apache/kafka

# Prometheus (监控)
docker run -d --name prometheus -p 9090:9090 prom/prometheus

# Grafana (监控面板)
docker run -d --name grafana -p 3000:3000 grafana/grafana

# Loki (日志聚合)
docker run -d --name loki -p 3100:3100 grafana/loki

# Zipkin (链路追踪)
docker run -d --name zipkin -p 9411:9411 openzipkin/zipkin
```

## 构建和运行

### 构建项目
```bash
mvn clean compile
```

### 运行应用
```bash
cd ainews-runr
mvn spring-boot:run
```

### 访问端点
- 应用健康检查: http://localhost:8080/api/v1/health
- 应用信息: http://localhost:8080/api/v1/info
- Actuator健康检查: http://localhost:8080/actuator/health
- Prometheus指标: http://localhost:8080/actuator/prometheus

## 配置说明

### 环境配置
- **开发环境**: `spring.profiles.active=dev`
- **生产环境**: `spring.profiles.active=prod`

### 关键配置
- Nacos服务地址: `localhost:8848` (开发) / `nacos:8848` (生产)
- Kafka地址: `localhost:9092` (开发) / `kafka:9092` (生产)
- Zipkin地址: `localhost:9411` (开发) / `zipkin:9411` (生产)

## 扩展模块

当前项目结构支持轻松添加新模块：

1. 在根目录创建新的子目录
2. 在父POM的`<modules>`中添加新模块
3. 创建子模块的pom.xml，引用父POM

## 开发指南

### 添加新的依赖
- 通用依赖添加到父POM的`<dependencyManagement>`
- 模块特定依赖添加到子模块POM的`<dependencies>`

### 日志配置
项目使用Logback配置，支持：
- 控制台输出
- Loki日志聚合
- 分布式追踪ID注入

### 监控配置
- Prometheus指标自动暴露
- 分布式追踪自动配置
- Actuator端点已启用

## 如何在代码中打日志（Log）
项目已集成 Spring Boot 默认日志（Logback），推荐使用 Lombok 的 @Slf4j 快速接入。

- 推荐方式（Lombok @Slf4j）：
  1) 在类上添加注解：`@Slf4j`
  2) 直接调用：
     - `log.trace("trace 日志: id={}", id);`
     - `log.debug("debug 日志: user={}, cost={}ms", user, costMs);`
     - `log.info("业务信息: {}", message);`
     - `log.warn("告警: key={}, reason={}", key, reason);`
     - `log.error("错误: ", ex);` 或 `log.error("错误: {}, code={}", msg, code, ex);`

  示例：
  ```java
  import lombok.extern.slf4j.Slf4j;
  @Slf4j
  public class DemoService {
      public void doSomething(String userId) {
          log.info("start doSomething, userId={} ", userId);
          try {
              // ... 业务逻辑
              log.debug("processing done");
          } catch (Exception ex) {
              log.error("doSomething failed, userId={}", userId, ex);
          }
      }
  }
  ```

- 非 Lombok 方式（也可用）：
  ```java
  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;

  public class DemoServiceNoLombok {
      private static final Logger log = LoggerFactory.getLogger(DemoServiceNoLombok.class);
  }
  ```

- 使用占位符，而不是字符串拼接：
  - 推荐：`log.info("userId={}, cost={}ms", userId, costMs);`
  - 不推荐：`log.info("userId=" + userId + ", cost=" + costMs + "ms");`

- 动态控制日志级别：
  - application.yml / application.properties 中设置：
    ```yaml
    logging:
      level:
        root: INFO
        com.mortis: DEBUG  # 指定你关注的包
    ```

- 已有示例：
  - EchoService: 使用 `@Slf4j` 的 `log.info` 打印任务执行时间。
  - HealthController（本次更新）: 在健康检查接口输出访问日志。
