package com.mortis.ainews.application.persistence.repository.interfaces;

import com.mortis.ainews.application.persistence.po.logs.AiNewsLog;
import com.mortis.ainews.domain.enums.LogTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Repository interface for AiNewsLog entity
 * Provides database operations for log management
 */
@Repository
public interface AiNewsLogRepository extends JpaRepository<AiNewsLog, Long> {

    /**
     * Find logs by log type
     * @param type log type
     * @param pageable pagination information
     * @return page of logs
     */
    Page<AiNewsLog> findByType(LogTypeEnum type, Pageable pageable);

    /**
     * Find logs by log level
     * @param level log level
     * @param pageable pagination information
     * @return page of logs
     */
    Page<AiNewsLog> findByLevel(String level, Pageable pageable);

    /**
     * Find logs by belong_id
     * @param belongId entity ID
     * @param pageable pagination information
     * @return page of logs
     */
    Page<AiNewsLog> findByBelongId(Long belongId, Pageable pageable);

    /**
     * Find logs by type and belong_id
     * @param type log type
     * @param belongId entity ID
     * @param pageable pagination information
     * @return page of logs
     */
    Page<AiNewsLog> findByTypeAndBelongId(LogTypeEnum type, Long belongId, Pageable pageable);

    /**
     * Find logs within a time range
     * @param startTime start time
     * @param endTime end time
     * @param pageable pagination information
     * @return page of logs
     */
    Page<AiNewsLog> findByCreatedAtBetween(Instant startTime, Instant endTime, Pageable pageable);

    /**
     * Find logs by type and time range
     * @param type log type
     * @param startTime start time
     * @param endTime end time
     * @param pageable pagination information
     * @return page of logs
     */
    Page<AiNewsLog> findByTypeAndCreatedAtBetween(LogTypeEnum type, Instant startTime, Instant endTime, Pageable pageable);

    /**
     * Count logs by type
     * @param type log type
     * @return count of logs
     */
    long countByType(LogTypeEnum type);

    /**
     * Count logs by level
     * @param level log level
     * @return count of logs
     */
    long countByLevel(String level);

    /**
     * Find recent logs by type (for monitoring purposes)
     * @param type log type
     * @param limit maximum number of logs to return
     * @return list of recent logs
     */
    @Query("SELECT l FROM AiNewsLog l WHERE l.type = :type ORDER BY l.createdAt DESC LIMIT :limit")
    List<AiNewsLog> findRecentLogsByType(@Param("type") LogTypeEnum type, @Param("limit") int limit);

    /**
     * Find error logs within time range (for alerting)
     * @param startTime start time
     * @param endTime end time
     * @return list of error logs
     */
    @Query("SELECT l FROM AiNewsLog l WHERE l.level = 'ERROR' AND l.createdAt BETWEEN :startTime AND :endTime ORDER BY l.createdAt DESC")
    List<AiNewsLog> findErrorLogsBetween(@Param("startTime") Instant startTime, @Param("endTime") Instant endTime);
}
