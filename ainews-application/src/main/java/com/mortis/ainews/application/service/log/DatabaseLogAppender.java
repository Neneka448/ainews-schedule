package com.mortis.ainews.application.service.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.mortis.ainews.domain.enums.LogTypeEnum;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom Logback appender that routes logs to database
 * Implements ApplicationContextAware to access Spring beans
 */
@Component
public class DatabaseLogAppender extends AppenderBase<ILoggingEvent> implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private DatabaseLogService databaseLogService;
    private boolean initialized = false;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Initialize the appender with Spring context
     */
    private void initializeIfNeeded() {
        if (!initialized && applicationContext != null) {
            try {
                this.databaseLogService = applicationContext.getBean(DatabaseLogService.class);
                this.initialized = true;
            } catch (Exception e) {
                // DatabaseLogService not available yet, will retry later
                addWarn("DatabaseLogService not available during initialization: " + e.getMessage());
            }
        }
    }

    @Override
    protected void append(ILoggingEvent event) {
        // Initialize if needed
        initializeIfNeeded();
        
        // Skip if service not available
        if (databaseLogService == null) {
            return;
        }

        try {
            // Extract log information
            String level = event.getLevel().toString();
            String message = event.getFormattedMessage();
            String loggerName = event.getLoggerName();
            
            // Create extra data with context information
            Map<String, Object> extraData = createExtraData(event);
            
            // Determine log type based on logger name or other criteria
            LogTypeEnum logType = determineLogType(loggerName);
            
            // Store to database asynchronously
            databaseLogService.storeLogAsync(level, message, logType, null, extraData);
            
        } catch (Exception e) {
            // Don't let logging errors affect the main application
            addError("Failed to append log to database", e);
        }
    }

    /**
     * Create extra data from logging event
     */
    private Map<String, Object> createExtraData(ILoggingEvent event) {
        Map<String, Object> extraData = new HashMap<>();
        
        // Add thread information
        extraData.put("thread_name", event.getThreadName());
        
        // Add logger information
        extraData.put("logger_name", event.getLoggerName());
        
        // Add MDC properties if available
        Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
        if (mdcPropertyMap != null && !mdcPropertyMap.isEmpty()) {
            extraData.put("mdc", mdcPropertyMap);
        }
        
        // Add exception information if present
        if (event.getThrowableProxy() != null) {
            extraData.put("exception_class", event.getThrowableProxy().getClassName());
            extraData.put("exception_message", event.getThrowableProxy().getMessage());
        }
        
        return extraData;
    }

    /**
     * Determine log type based on logger name or other criteria
     * This can be extended to support different log types based on package names
     */
    private LogTypeEnum determineLogType(String loggerName) {
        // For now, all logs are COMMON type
        // Future enhancement: determine type based on package name
        // Example:
        // if (loggerName.contains("schedule")) return LogTypeEnum.SCHEDULE;
        // if (loggerName.contains("user")) return LogTypeEnum.USER;
        
        return LogTypeEnum.COMMON;
    }

    @Override
    public void start() {
        super.start();
        addInfo("DatabaseLogAppender started");
    }

    @Override
    public void stop() {
        super.stop();
        addInfo("DatabaseLogAppender stopped");
    }
}
