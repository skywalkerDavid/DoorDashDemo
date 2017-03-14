package com.skywalker.doordashdemo;

import android.content.SharedPreferences;
import dagger.Component;
import javax.inject.Singleton;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {
    void inject(DoorDashApplication application);

    Retrofit retrofit();
    SharedPreferences sharedPreferences();
}
