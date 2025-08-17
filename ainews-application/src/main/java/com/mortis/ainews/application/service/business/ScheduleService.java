package com.mortis.ainews.application.service.business;

import com.mortis.ainews.application.persistence.converter.facade.ConverterFacade;
import com.mortis.ainews.application.persistence.po.keywords.Keyword;
import com.mortis.ainews.application.persistence.repository.interfaces.*;
import com.mortis.ainews.domain.model.KeywordDO;
import com.mortis.ainews.domain.model.ScheduleDO;
import com.mortis.ainews.domain.model.UserScheduleRelDO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserScheduleRepository userScheduleRepository;
    private final KeywordScheduleRepository keywordScheduleRepository;
    private final KeywordRepository keywordRepository;
    private final ConverterFacade converterFacade;

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional(rollbackFor = Exception.class)
    public ScheduleDO createSchedule(ScheduleDO schedule, Long userId) {
        var res = scheduleRepository.save(converterFacade.scheduleConverter.toPO(schedule));
        userScheduleRepository.save(converterFacade.userScheduleRelConverter.toPO(UserScheduleRelDO
            .builder()
            .userId(userId)
            .scheduleId(res.getId())
            .build()
        ));
        return converterFacade.scheduleConverter.toDO(res);
    }

    public ScheduleDO getScheduleById(Long scheduleId) {
        return scheduleRepository
            .findById(scheduleId)
            .map(converterFacade.scheduleConverter::toDO)
            .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + scheduleId));
    }

    public List<KeywordDO> getKeywordsByScheduleId(Long scheduleId) {
        String jpql = "SELECT k FROM KeywordScheduleRel ksr " +
            "JOIN Keyword k ON k.id = ksr.id.keywordId " +
            "WHERE ksr.id.scheduleId = :scheduleId AND ksr.deleted = 0 AND k.deleted = 0";

        List<Keyword> keywords = entityManager
            .createQuery(
                jpql,
                Keyword.class
            )
            .setParameter(
                "scheduleId",
                scheduleId
            )
            .getResultList();

        return keywords
            .stream()
            .map(converterFacade.keywordConverter::toDO)
            .collect(Collectors.toList());

    }


}
