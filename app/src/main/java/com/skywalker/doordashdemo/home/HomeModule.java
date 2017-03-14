package com.skywalker.doordashdemo.home;

import com.skywalker.doordashdemo.PerActivity;
import com.skywalker.doordashdemo.api.RestaurantApiClient;
import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {

    @Provides
    @PerActivity
    HomeMVPContractor.Presenter providePresenter(final RestaurantApiClient restaurantApiClient) {
        return new HomePresenter(restaurantApiClient);
    }
}
