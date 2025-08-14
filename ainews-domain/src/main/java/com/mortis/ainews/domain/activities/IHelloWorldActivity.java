package com.mortis.ainews.domain.activities;


import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface IHelloWorldActivity {
    @ActivityMethod
    String say(String name);
}
