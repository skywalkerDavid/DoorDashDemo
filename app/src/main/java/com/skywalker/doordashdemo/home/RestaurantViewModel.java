package com.skywalker.doordashdemo.home;

import com.skywalker.doordashdemo.api.Restaurant;
import java.util.List;

public class RestaurantViewModel {
    public int id;
    public String name;
    public String status;
    public List<String> tags;
    public int priceRange;
    public String imageUrl;
    public boolean isFavorite;

    public static RestaurantViewModel convert(Restaurant restaurant) {
        RestaurantViewModel viewModel = new RestaurantViewModel();
        viewModel.id = restaurant.id;
        viewModel.name = restaurant.name;
        viewModel.status = restaurant.status;
        viewModel.tags = restaurant.tags;
        viewModel.priceRange = restaurant.priceRange;
        viewModel.imageUrl = restaurant.imageUrl;
        return viewModel;
    }
}
