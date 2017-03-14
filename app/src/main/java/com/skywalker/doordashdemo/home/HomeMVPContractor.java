package com.skywalker.doordashdemo.home;

import java.util.List;

public interface HomeMVPContractor {
    interface View {
        void showRestaurants(List<RestaurantViewModel> restaurants);
        void showRefreshing();
        void showError();
        void updateFavorite(RestaurantViewModel restaurant, int position);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void onCreate();
        void onDestroy();
        void refreshData();
        void onClickFavorite(int position);
    }
}
