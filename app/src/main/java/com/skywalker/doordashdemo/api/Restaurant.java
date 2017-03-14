package com.skywalker.doordashdemo.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Restaurant {
    public int id;
    public String name;
    public String status;
    public List<String> tags;

    @SerializedName("price_range")
    public int priceRange;

    @SerializedName("cover_img_url")
    public String imageUrl;
}
