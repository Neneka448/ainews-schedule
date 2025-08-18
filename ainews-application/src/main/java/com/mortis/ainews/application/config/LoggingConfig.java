package com.mortis.ainews.application.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mortis.ainews.application.service.log.DatabaseLogAppender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuration for custom logging setup
 * Configures database logging appender while maintaining existing console and Loki logging
 */
@Configuration
@EnableAsync
@RequiredArgsConstructor
@Slf4j
public class LoggingConfig {

    private final ApplicationContext applicationContext;

    /**
     * Configure database logging appender after application is ready
     * This ensures all Spring beans are initialized before setting up the appender
     */
    @EventListener(ApplicationReadyEvent.class)
    public void configureDatabaseLogging() {
        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            
            // Create and configure database appender
            DatabaseLogAppender databaseAppender = new DatabaseLogAppender();
            databaseAppender.setApplicationContext(applicationContext);
            databaseAppender.setContext(loggerContext);
            databaseAppender.setName("DATABASE");
            
            // Start the appender
            databaseAppender.start();
            
            // Add appender to root logger to capture all logs
            Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
            rootLogger.addAppender(databaseAppender);
            
            // Optionally, add to specific loggers for more targeted logging
            // Logger appLogger = loggerContext.getLogger("com.mortis.ainews");
            // appLogger.addAppender(databaseAppender);
            
            log.info("Database logging appender configured successfully");
            
        } catch (Exception e) {
            log.error("Failed to configure database logging appender", e);
        }
    }
}
