package com.skywalker.doordashdemo.home;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.skywalker.doordashdemo.R;
import java.util.List;

public class RestaurantCardView extends FrameLayout {
    private static int defaultImageHeight;
    private static int defaultImageWidth;

    @BindView(R.id.resturant_image) ImageView imageView;
    @BindView(R.id.resturant_title) TextView titleTextView;
    @BindView(R.id.resturant_tags) TextView tagsTextView;
    @BindView(R.id.resturant_price) TextView priceTextView;
    @BindView(R.id.resturant_status) TextView statusTextView;
    @BindView(R.id.add_to_favorite) ImageButton addToFavoriteButton;

    public RestaurantCardView(@NonNull Context context) {
        super(context);
        onFinishInflate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        inflate(getContext(), R.layout.restaurant_card, this);
        ButterKnife.bind(this);
    }

    public static void setDefaultImageSize(final Resources resources) {
        defaultImageWidth = resources.getDisplayMetrics().widthPixels / 3 - resources.getDimensionPixelSize(R.dimen.card_padding_left_right);
        defaultImageHeight = (int) (defaultImageWidth / resources.getFraction(R.fraction.card_image_aspect_ratio, 1, 1));
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return;
        }

        final StringBuilder stringBuilder = new StringBuilder();
        final int size = tags.size();
        for (int i = 0; i < size; ++i) {
            stringBuilder.append(tags.get(i));
            if (i < size-1) {
                stringBuilder.append(",");
            }
        }
        tagsTextView.setText(stringBuilder.toString());
    }

    public void setPrice(String price) {
        priceTextView.setText(price);
    }

    public void setStatus(String status) {
        statusTextView.setText(status);
    }

    public void setImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }
        Glide.with(getContext().getApplicationContext())
            .load(imageUrl)
            .override(defaultImageWidth, defaultImageHeight)
            .centerCrop()
            .dontAnimate()
            .into(imageView);
    }

    public void setFavorite(boolean isFavorite) {
        if (isFavorite) {
            addToFavoriteButton.setImageResource(R.drawable.ic_favorite_selected);
        } else {
            addToFavoriteButton.setImageResource(R.drawable.ic_favorite_unselected);
        }
    }

    public void setAddToFavoriteButtonClickListener(final View.OnClickListener onClickListener) {
        addToFavoriteButton.setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
        });
    }
}
