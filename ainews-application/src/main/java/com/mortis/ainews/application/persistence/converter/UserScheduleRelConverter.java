package com.mortis.ainews.application.persistence.converter;

import com.mortis.ainews.application.persistence.po.schedules.UserScheduleId;
import com.mortis.ainews.application.persistence.po.schedules.UserScheduleRel;
import com.mortis.ainews.domain.model.UserScheduleRelDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserScheduleRelConverter {

    @Mapping(target = "userId", source = "id.userId")
    @Mapping(target = "scheduleId", source = "id.scheduleId")
    UserScheduleRelDO toUserScheduleRelDO(UserScheduleRel userScheduleRel);

    @Mapping(target = "id.userId", source = "userId")
    @Mapping(target = "id.scheduleId", source = "scheduleId")
    UserScheduleRel toUserScheduleRel(UserScheduleRelDO userScheduleRelDO);

    default UserScheduleId createUserScheduleId(Long userId, Long scheduleId) {
        if (userId == null || scheduleId == null) {
            return null;
        }
        return new UserScheduleId(
                userId,
                scheduleId
        );
    }
}
