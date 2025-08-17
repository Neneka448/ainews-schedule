package com.mortis.ainews.application.persistence.repository.interfaces;

import com.mortis.ainews.application.persistence.po.search.AiNewsSearchContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiNewsSearchContentRepository extends JpaRepository<AiNewsSearchContent, Long> {
    
    /**
     * Find all search content by schedule ID
     */
    List<AiNewsSearchContent> findByScheduleId(Long scheduleId);
    
    /**
     * Find all search content by keyword ID
     */
    List<AiNewsSearchContent> findByKeywordId(Long keywordId);
    
    /**
     * Find all search content by schedule ID and keyword ID
     */
    List<AiNewsSearchContent> findByScheduleIdAndKeywordId(Long scheduleId, Long keywordId);
}
