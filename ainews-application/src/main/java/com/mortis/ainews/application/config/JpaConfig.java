package com.mortis.ainews.application.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA 配置类
 * 配置JPA Repository扫描路径和实体类扫描路径
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.mortis.ainews.application.persistence.repository.interfaces")
@EntityScan(basePackages = "com.mortis.ainews.application.persistence.po")
@EnableTransactionManagement
public class JpaConfig {
}
