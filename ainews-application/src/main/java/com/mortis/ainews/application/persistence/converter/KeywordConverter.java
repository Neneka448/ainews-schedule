package com.mortis.ainews.application.persistence.converter;

import org.mapstruct.Mapper;

import java.util.List;

import com.mortis.ainews.application.persistence.po.keywords.Keyword;
import com.mortis.ainews.domain.model.KeywordDO;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public interface KeywordConverter {
    KeywordDO toDO(Keyword keyword);

    List<KeywordDO> toDOs(List<Keyword> keywords);

    Keyword toPO(KeywordDO keywordDO);

    List<Keyword> toPOs(List<KeywordDO> keywordDOs);
}