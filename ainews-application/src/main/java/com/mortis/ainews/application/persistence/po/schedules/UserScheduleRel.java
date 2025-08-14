package com.mortis.ainews.application.persistence.po.schedules;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ai_news_user_schedule")
public class UserScheduleRel {
    @EmbeddedId
    private UserScheduleId id;
    @Column(name = "deleted")
    private int deleted;

}