package com.skywalker.doordashdemo.home;

import com.skywalker.doordashdemo.ApplicationComponent;
import com.skywalker.doordashdemo.PerActivity;
import com.skywalker.doordashdemo.api.ApiModule;
import dagger.Component;

@PerActivity
@Component(
    dependencies = {
        ApplicationComponent.class
    },
    modules = {
        HomeModule.class,
        ApiModule.class
    }
)
public interface HomeComponent {
    void inject(HomeActivity homeActivity);
}