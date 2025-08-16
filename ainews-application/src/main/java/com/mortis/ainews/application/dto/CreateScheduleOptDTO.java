package com.mortis.ainews.application.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateScheduleOptDTO {
    private Optional<Boolean> immediate = Optional.empty(); // 默认初始化为empty
}
