package com.skywalker.doordashdemo.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface RestaurantApiClient {
    @GET("/v2/restaurant/")
    Observable<Restaurants> listRestaurants(@Query("lat") String lat, @Query("lng") String lng);
}
