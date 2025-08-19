package com.mortis.ainews.domain.model;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class KeywordRelatedContent {
    KeywordDO keywordDO;
    List<String> contents;
}
