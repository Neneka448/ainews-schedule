package com.mortis.ainews.application.helper;


import com.mortis.ainews.application.enums.TemporalTaskTypeEnum;
import com.mortis.ainews.application.task.activities.HelloWorldActivityImpl;
import com.mortis.ainews.application.task.workflow.InfoProcessWorkflowImpl;
import io.temporal.worker.Worker;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Data
@Slf4j
public class TemporalWorkflowMappingHelper {
    private final HelloWorldActivityImpl helloWorldActivityImpl;

    private final Map<TemporalTaskTypeEnum, Class<?>> workflowMap = Map.ofEntries(
            Map.entry(
                    TemporalTaskTypeEnum.SEND_USER_NEWS_BY_EMAIL,
                    InfoProcessWorkflowImpl.class
            )
    );

    public void registerWorkflowToWorker(Worker worker) {
        workflowMap.values()
                .forEach(worker::registerWorkflowImplementationTypes);
    }

    public void registerActivitiesToWorker(Worker worker) {
        // 使用反射获取所有字段
        Field[] fields = this.getClass()
                .getDeclaredFields();

        for (Field field : fields) {
            try {
                // 设置字段可访问
                field.setAccessible(true);
                Object fieldValue = field.get(this);

                // 检查字段值是否不为空，并且字段名以 ActivityImpl 结尾（Activity实现的命名约定）
                if (fieldValue != null && field.getName()
                        .endsWith("ActivityImpl"))
                {
                    worker.registerActivitiesImplementations(fieldValue);
                    log.info(
                            "Registered activity: {} to worker",
                            fieldValue.getClass()
                                    .getSimpleName()
                    );
                }
            } catch (IllegalAccessException e) {
                log.error(
                        "Failed to access field: {}",
                        field.getName(),
                        e
                );
            }
        }
    }


    public Optional<Class<?>> getWorkflowClassByType(TemporalTaskTypeEnum type) {
        return Optional.ofNullable(workflowMap.get(type));
    }

    public Optional<Class<?>> getInterfaceClassByType(TemporalTaskTypeEnum type) {
        var interfaces = workflowMap.get(type)
                .getInterfaces();
        if (interfaces.length > 0) {
            return Optional.of(interfaces[0]);
        }
        return Optional.empty();
    }
}
