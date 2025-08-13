package com.mortis.ainews.application.config;


import com.mortis.ainews.application.task.activities.HelloWorldActivityImpl;
import io.temporal.worker.Worker;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@RequiredArgsConstructor
@Data
@Slf4j
public class TemporalMap {
    private final HelloWorldActivityImpl helloWorldActivityImpl;

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
}
