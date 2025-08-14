package com.mortis.ainews.application.persistence.po.schedules;

import com.mortis.ainews.domain.enums.TemporalTaskTypeEnum;
import com.mortis.ainews.domain.model.ScheduleSpecDO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ai_news_schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Lob
    @Column(name = "prompt")
    private String prompt;

    @NotNull
    @Column(name = "spec", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private ScheduleSpecDO spec;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "workflow_type", nullable = false, length = 64)
    private TemporalTaskTypeEnum workflowType;

    @Column(name = "deleted")
    private int deleted;

}