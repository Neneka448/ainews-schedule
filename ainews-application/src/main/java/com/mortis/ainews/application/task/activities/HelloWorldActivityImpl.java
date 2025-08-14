package com.mortis.ainews.application.task.activities;


import com.mortis.ainews.domain.activities.IHelloWorldActivity;
import org.springframework.stereotype.Component;


@Component
public class HelloWorldActivityImpl implements IHelloWorldActivity {
    @Override
    public String say(String name) {
        System.out.println("Hello " + name);
        return "Hello " + name;
    }
}