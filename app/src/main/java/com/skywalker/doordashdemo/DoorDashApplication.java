package com.skywalker.doordashdemo;

import android.app.Application;
import com.skywalker.doordashdemo.home.RestaurantCardView;

public class DoorDashApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .networkModule(new NetworkModule())
            .build();

        // preset the image size
        RestaurantCardView.setDefaultImageSize(getResources());
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
