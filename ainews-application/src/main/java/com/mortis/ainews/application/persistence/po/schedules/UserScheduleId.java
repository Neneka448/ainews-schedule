package com.mortis.ainews.application.persistence.po.schedules;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserScheduleId implements Serializable {
    @Serial
    private static final long serialVersionUID = 7150111488492261670L;
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        UserScheduleId entity = (UserScheduleId) o;
        return Objects.equals(
                this.userId,
                entity.userId
        ) &&
                Objects.equals(
                        this.scheduleId,
                        entity.scheduleId
                );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                userId,
                scheduleId
        );
    }

}