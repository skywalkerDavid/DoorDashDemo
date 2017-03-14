package com.skywalker.doordashdemo.home;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.skywalker.doordashdemo.R;
import java.util.ArrayList;
import java.util.List;

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter {
    public interface OnFavoriteClickListener {

        void onClick(int position);
    }

    private static final int CARD_VIEW_TYPE = 0;
    private static final int EMPTY_VIEW_TYPE = 1;

    private final List<RestaurantViewModel> restaurants = new ArrayList<>();

    @Nullable private OnFavoriteClickListener onFavoriteClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case CARD_VIEW_TYPE:
                final RestaurantCardView view = new RestaurantCardView(parent.getContext());
                return new RestaurantCardViewHolder(view);
            case EMPTY_VIEW_TYPE:
                return new EmptyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_list, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) != CARD_VIEW_TYPE) {
            return;
        }

        viewHolder.itemView.setTag(position);

        final RestaurantViewModel restaurant = getItem(position);
        RestaurantCardView cardView = (RestaurantCardView) viewHolder.itemView;
        cardView.setTitle(restaurant.name);
        cardView.setImage(restaurant.imageUrl);
        cardView.setTags(restaurant.tags);
        cardView.setStatus(restaurant.status);
        cardView.setPrice("$" + (restaurant.priceRange * 1.0f - 0.01f));
        cardView.setFavorite(restaurant.isFavorite);
        cardView.setAddToFavoriteButtonClickListener(new InternalOnClickFavoriteListener(position));
    }

    @Override
    public int getItemViewType(final int position) {
        if (getItemCount() == 0) {
            return EMPTY_VIEW_TYPE;
        }

        return CARD_VIEW_TYPE;
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void setOnFavioriteClickListener(@Nullable OnFavoriteClickListener listener) {
        this.onFavoriteClickListener = listener;
    }

    public void setRestaurants(List<RestaurantViewModel> restaurants) {
        final int oldDealsSize = this.restaurants.size();
        final int newDealsSize = restaurants.size();

        this.restaurants.clear();
        this.restaurants.addAll(restaurants);

        if (oldDealsSize < newDealsSize) {
            notifyItemRangeChanged(0, oldDealsSize);
            notifyItemRangeInserted(oldDealsSize, newDealsSize - oldDealsSize);
        } else if (oldDealsSize == newDealsSize) {
            notifyItemRangeChanged(0, newDealsSize);
        } else {
            notifyItemRangeChanged(0, newDealsSize);
            notifyItemRangeRemoved(newDealsSize, oldDealsSize - newDealsSize);
        }
    }

    public void setRestaurant(RestaurantViewModel restaurant, int position) {
        this.restaurants.set(position, restaurant);
        notifyItemRangeChanged(position, 1);
    }

    public void clearRestaurants() {
        final int itemCount = restaurants.size();

        this.restaurants.clear();

        notifyItemRangeRemoved(0, itemCount);
    }

    private RestaurantViewModel getItem(int position) {
        return restaurants.get(position);
    }

    private class InternalOnClickFavoriteListener implements View.OnClickListener {

        private final int position;

        private InternalOnClickFavoriteListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (onFavoriteClickListener != null) {
                onFavoriteClickListener.onClick(position);
            }
        }
    }
}
