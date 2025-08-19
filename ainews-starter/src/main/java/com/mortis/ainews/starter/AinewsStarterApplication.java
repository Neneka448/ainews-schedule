package com.mortis.ainews.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan("com.mortis.ainews")
public class AinewsStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            AinewsStarterApplication.class,
            args
        );
    }

}
