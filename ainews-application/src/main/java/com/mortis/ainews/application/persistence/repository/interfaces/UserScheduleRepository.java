package com.mortis.ainews.application.persistence.repository.interfaces;

import com.mortis.ainews.application.persistence.po.schedules.Schedule;
import com.mortis.ainews.application.persistence.po.schedules.UserScheduleId;
import com.mortis.ainews.application.persistence.po.schedules.UserScheduleRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserScheduleRepository extends JpaRepository<UserScheduleRel, UserScheduleId> {
}
