package com.mortis.ainews.application.helper;


import com.mortis.ainews.application.task.activities.InfoProcessActivitiesImpl;
import com.mortis.ainews.domain.enums.TemporalTaskTypeEnum;
import com.mortis.ainews.application.task.activities.HelloWorldActivityImpl;
import com.mortis.ainews.application.task.workflow.InfoProcessWorkflowImpl;
import io.temporal.activity.ActivityInterface;
import io.temporal.worker.Worker;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Optional;

import com.mortis.ainews.application.enums.TemporalNamespaceEnum;

@Component
@RequiredArgsConstructor
@Data
@Slf4j
public class TemporalWorkflowMappingHelper {
    private final HelloWorldActivityImpl helloWorldActivityImpl;
    private final InfoProcessActivitiesImpl infoProcessActivitiesImpl;

    private final Map<TemporalTaskTypeEnum, Class<?>> workflowMap = Map.ofEntries(
        Map.entry(
            TemporalTaskTypeEnum.SEND_USER_NEWS_BY_EMAIL,
            InfoProcessWorkflowImpl.class
        )
    );

    // 为每个工作流类型配置对应的 task queue（与 WorkerConfig 中一致）
    private final Map<TemporalTaskTypeEnum, String> taskQueueMap = Map.ofEntries(
        Map.entry(
            TemporalTaskTypeEnum.SEND_USER_NEWS_BY_EMAIL,
            TemporalNamespaceEnum.Job.getValue()
        )
    );

    public void registerWorkflowToWorker(Worker worker) {
        workflowMap
            .values()
            .forEach(worker::registerWorkflowImplementationTypes);
    }

    public void registerActivitiesToWorker(Worker worker) {
        // 使用反射获取所有字段
        Field[] fields = this
            .getClass()
            .getDeclaredFields();

        for (Field field : fields) {
            try {
                // 跳过静态字段
                if (Modifier.isStatic(field.getModifiers()))
                    continue;

                field.setAccessible(true);
                Object fieldValue = field.get(this);
                if (fieldValue == null)
                    continue;

                if (isActivityImplementation(fieldValue)) {
                    worker.registerActivitiesImplementations(fieldValue);
                    log.info(
                        "Registered activity: {} to worker",
                        fieldValue
                            .getClass()
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

    private boolean isActivityImplementation(Object impl) {
        Class<?> clazz = impl.getClass();
        for (Class<?> iface : clazz.getInterfaces()) {
            if (iface.isAnnotationPresent(ActivityInterface.class)) {
                return true;
            }
        }
        return false;
    }

    public Optional<Class<?>> getWorkflowClassByType(TemporalTaskTypeEnum type) {
        return Optional.ofNullable(workflowMap.get(type));
    }

    public Optional<Class<?>> getInterfaceClassByType(TemporalTaskTypeEnum type) {
        var interfaces = workflowMap
            .get(type)
            .getInterfaces();
        if (interfaces.length > 0) {
            return Optional.of(interfaces[0]);
        }
        return Optional.empty();
    }

    public String getTaskQueueByType(TemporalTaskTypeEnum type) {
        return Optional
            .ofNullable(taskQueueMap.get(type))
            .orElse(TemporalNamespaceEnum.Job.getValue());
    }
}
