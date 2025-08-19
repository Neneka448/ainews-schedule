package com.mortis.ainews.application.service.business;

import com.mortis.ainews.application.persistence.converter.facade.ConverterFacade;
import com.mortis.ainews.application.persistence.po.keywords.Keyword;
import com.mortis.ainews.application.persistence.po.schedules.KeywordScheduleId;
import com.mortis.ainews.application.persistence.po.schedules.KeywordScheduleRel;
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

    @Transactional(rollbackFor = Exception.class)
    public ScheduleDO createScheduleWithKeywords(ScheduleDO schedule, Long userId, List<Long> keywordIds) {
        // 创建 Schedule 和 UserScheduleRel
        var res = scheduleRepository.save(converterFacade.scheduleConverter.toPO(schedule));
        userScheduleRepository.save(converterFacade.userScheduleRelConverter.toPO(UserScheduleRelDO
            .builder()
            .userId(userId)
            .scheduleId(res.getId())
            .build()
        ));

        // 创建 KeywordScheduleRel 关联关系
        if (keywordIds != null && !keywordIds.isEmpty()) {
            createKeywordScheduleRelations(res.getId(), keywordIds);
        }

        return converterFacade.scheduleConverter.toDO(res);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createKeywordScheduleRelations(Long scheduleId, List<Long> keywordIds) {
        log.info("Creating keyword-schedule relations for schedule: {} with keywords: {}",
            scheduleId, keywordIds);

        // 验证关键词是否存在
        List<Long> existingKeywordIds = keywordRepository.findByIdInAndDeleted(keywordIds, 0)
            .stream()
            .map(keyword -> keyword.getId())
            .toList();

        if (existingKeywordIds.size() != keywordIds.size()) {
            List<Long> missingIds = keywordIds.stream()
                .filter(id -> !existingKeywordIds.contains(id))
                .toList();
            log.warn("Some keyword IDs do not exist or are deleted: {}", missingIds);
        }

        // 批量创建关联关系
        List<KeywordScheduleRel> relations = existingKeywordIds.stream()
            .map(keywordId -> new KeywordScheduleRel(
                new KeywordScheduleId(keywordId, scheduleId),
                0 // deleted = 0
            ))
            .toList();

        keywordScheduleRepository.saveAll(relations);
        log.info("Successfully created {} keyword-schedule relations", relations.size());
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
