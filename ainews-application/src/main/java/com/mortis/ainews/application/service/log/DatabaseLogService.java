package com.mortis.ainews.application.service.log;

import com.mortis.ainews.application.persistence.po.logs.AiNewsLog;
import com.mortis.ainews.application.persistence.repository.interfaces.AiNewsLogRepository;
import com.mortis.ainews.domain.enums.LogTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Service for storing logs to database
 * Integrates with SLF4J logging framework and provides database persistence
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseLogService {

    private final AiNewsLogRepository logRepository;
    private final LogHelper logHelper;

    /**
     * Store a log entry to database asynchronously
     * Uses separate transaction to avoid affecting main business logic
     * 
     * @param level SLF4J log level (TRACE, DEBUG, INFO, WARN, ERROR)
     * @param message log message
     * @param logType log type categorization
     * @param belongId entity ID (optional, will use default if null)
     * @param extraData additional log data (optional)
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void storeLogAsync(String level, String message, LogTypeEnum logType, Long belongId, Map<String, Object> extraData) {
        try {
            // Resolve belong_id using LogHelper
            Long resolvedBelongId = logHelper.resolveBelongId(logType, belongId);
            
            // Parse and validate extra data
            Map<String, Object> processedExtra = logHelper.parseExtraData(logType, extraData);
            
            // Create and save log entry
            AiNewsLog logEntry = new AiNewsLog(level, logType, message, resolvedBelongId, processedExtra);
            logRepository.save(logEntry);
            
        } catch (Exception e) {
            // Log the error but don't throw to avoid affecting main application flow
            log.error("Failed to store log to database: level={}, message={}, type={}", 
                level, message, logType, e);
        }
    }

    /**
     * Store a simple COMMON type log entry
     * 
     * @param level SLF4J log level
     * @param message log message
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void storeCommonLogAsync(String level, String message) {
        storeLogAsync(level, message, LogTypeEnum.COMMON, null, null);
    }

    /**
     * Store a log entry synchronously (for critical logs that must be persisted)
     * 
     * @param level SLF4J log level
     * @param message log message
     * @param logType log type categorization
     * @param belongId entity ID (optional)
     * @param extraData additional log data (optional)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void storeLogSync(String level, String message, LogTypeEnum logType, Long belongId, Map<String, Object> extraData) {
        try {
            Long resolvedBelongId = logHelper.resolveBelongId(logType, belongId);
            Map<String, Object> processedExtra = logHelper.parseExtraData(logType, extraData);
            
            AiNewsLog logEntry = new AiNewsLog(level, logType, message, resolvedBelongId, processedExtra);
            logRepository.save(logEntry);
            
        } catch (Exception e) {
            log.error("Failed to store log to database synchronously: level={}, message={}, type={}", 
                level, message, logType, e);
            throw e; // Re-throw for synchronous calls
        }
    }

    /**
     * Validate SLF4J log level
     * 
     * @param level log level string
     * @return true if valid SLF4J level
     */
    public boolean isValidLogLevel(String level) {
        if (level == null) {
            return false;
        }
        
        switch (level.toUpperCase()) {
            case "TRACE":
            case "DEBUG":
            case "INFO":
            case "WARN":
            case "ERROR":
                return true;
            default:
                return false;
        }
    }

    /**
     * Normalize log level to uppercase
     * 
     * @param level log level string
     * @return normalized log level
     */
    public String normalizeLogLevel(String level) {
        if (level == null) {
            return "INFO"; // Default level
        }
        
        String normalized = level.toUpperCase();
        return isValidLogLevel(normalized) ? normalized : "INFO";
    }

    /**
     * Create a log context and store log entry
     * 
     * @param level SLF4J log level
     * @param message log message
     * @param logType log type
     * @param entityId entity ID for context
     * @param additionalContext additional context data
     */
    @Async
    public void storeLogWithContext(String level, String message, LogTypeEnum logType, 
                                   Long entityId, Map<String, Object> additionalContext) {
        LogHelper.LogContext context = logHelper.createLogContext(logType, entityId, additionalContext);
        storeLogAsync(level, message, context.getLogType(), context.getBelongId(), context.getExtraData());
    }

    /**
     * Store a COMMON type log with normalized level
     * 
     * @param level log level (will be normalized)
     * @param message log message
     */
    public void storeCommonLog(String level, String message) {
        String normalizedLevel = normalizeLogLevel(level);
        storeCommonLogAsync(normalizedLevel, message);
    }
}
