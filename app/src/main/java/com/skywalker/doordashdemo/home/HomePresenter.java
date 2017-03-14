package com.skywalker.doordashdemo.home;

import android.support.annotation.VisibleForTesting;
import android.support.v4.util.Pair;
import com.skywalker.doordashdemo.api.Restaurant;
import com.skywalker.doordashdemo.api.RestaurantApiClient;
import com.skywalker.doordashdemo.api.Restaurants;
import java.util.ArrayList;
import java.util.List;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomePresenter implements HomeMVPContractor.Presenter {

    @VisibleForTesting HomeMVPContractor.View view;
    @VisibleForTesting List<RestaurantViewModel> restaurantViewModels = new ArrayList<>();
    @VisibleForTesting boolean isRefreshing;
    private RestaurantApiClient restaurantApiClient;
    private Subscription subscription;

    public HomePresenter(RestaurantApiClient restaurantApiClient) {
        this.restaurantApiClient = restaurantApiClient;
    }

    @Override
    public void attachView(HomeMVPContractor.View view) {
        this.view = view;
        if (isRefreshing) {
            this.view.showRefreshing();
        }
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void onCreate() {
        refreshData();
    }

    @Override
    public void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onClickFavorite(int position) {
        // Call the API to add the restaurant to favorite list
        final RestaurantViewModel restaurant = restaurantViewModels.get(position);
        restaurant.isFavorite = !restaurant.isFavorite;

        if (view != null) {
            view.updateFavorite(restaurant, position);
        }
    }

    @Override
    public void refreshData() {
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;

        if (view != null) {
            view.showRefreshing();
        }

        final Pair<String, String> latLngPair = getLocation();

        subscription = restaurantApiClient.listRestaurants(latLngPair.first, latLngPair.second)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .cache()
            .subscribe(this::handleRestaurantsResponse,
                       this::handleErrorResponse);
    }

    private Pair<String, String> getLocation() {
        // Use fixed location
        return new Pair<>("37.422740", "-122.139956");
    }

    private void handleRestaurantsResponse(Restaurants restaurants) {
        isRefreshing = false;
        restaurantViewModels.clear();
        for (Restaurant restaurant : restaurants) {
            restaurantViewModels.add(RestaurantViewModel.convert(restaurant));
        }
        if (view != null) {
            view.showRestaurants(restaurantViewModels);
        }
    }

    private void handleErrorResponse(Throwable throwable) {
        isRefreshing = false;
        throwable.printStackTrace();
        restaurantViewModels.clear();
        if (view != null) {
            view.showError();
        }
    }
}
