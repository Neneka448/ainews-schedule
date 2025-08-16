package com.mortis.ainews.application.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 配置Jdk8Module，设置缺失字段时使用Optional.empty()
        Jdk8Module jdk8Module = new Jdk8Module();
        jdk8Module.configureAbsentsAsNulls(false); // 缺失字段不设为null
        mapper.registerModule(jdk8Module);

        return mapper;
    }

}
