package com.skywalker.doordashdemo.api;

import com.skywalker.doordashdemo.PerActivity;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApiModule {

    @Provides
    @PerActivity
    RestaurantApiClient provideRestaurantApiClient(Retrofit retrofit) {
        return retrofit.create(RestaurantApiClient.class);
    }
}
