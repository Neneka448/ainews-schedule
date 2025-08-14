package com.mortis.ainews.application.persistence.po.schedules;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class KeywordScheduleId implements Serializable {
    @Serial
    private static final long serialVersionUID = -9165727258779022305L;
    @NotNull
    @Column(name = "keyword_id", nullable = false)
    private Long keywordId;

    @NotNull
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        KeywordScheduleId entity = (KeywordScheduleId) o;
        return Objects.equals(
                this.keywordId,
                entity.keywordId
        ) &&
                Objects.equals(
                        this.scheduleId,
                        entity.scheduleId
                );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                keywordId,
                scheduleId
        );
    }

}