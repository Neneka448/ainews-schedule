package com.mortis.ainews.application.persistence.repository.interfaces;

import com.mortis.ainews.application.persistence.po.schedules.KeywordScheduleId;
import com.mortis.ainews.application.persistence.po.schedules.KeywordScheduleRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface KeywordScheduleRepository extends JpaRepository<KeywordScheduleRel, KeywordScheduleId> {
}
