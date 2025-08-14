package com.mortis.ainews.application.persistence.repository.interfaces;

import com.mortis.ainews.application.persistence.po.schedules.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
