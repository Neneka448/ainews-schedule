package com.mortis.ainews.domain.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonSerialize
public class InfoProcessWorkflowParams {
    private Long userId;
    private Long scheduleId;
}
