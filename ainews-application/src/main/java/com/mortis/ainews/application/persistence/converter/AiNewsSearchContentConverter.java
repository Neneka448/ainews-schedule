package com.mortis.ainews.application.persistence.converter;

import com.mortis.ainews.application.persistence.po.search.AiNewsSearchContent;
import com.mortis.ainews.domain.model.AiNewsSearchContentDO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AiNewsSearchContentConverter {

    AiNewsSearchContentDO toDO(AiNewsSearchContent aiNewsSearchContent);

    List<AiNewsSearchContentDO> toDOs(List<AiNewsSearchContent> aiNewsSearchContents);

    AiNewsSearchContent toPO(AiNewsSearchContentDO aiNewsSearchContentDO);

    List<AiNewsSearchContent> toPOs(List<AiNewsSearchContentDO> aiNewsSearchContentDOs);
}
