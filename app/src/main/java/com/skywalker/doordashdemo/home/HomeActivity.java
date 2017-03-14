package com.skywalker.doordashdemo.home;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.skywalker.doordashdemo.DoorDashApplication;
import com.skywalker.doordashdemo.R;
import com.skywalker.doordashdemo.api.ApiModule;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.inject.Inject;

public class HomeActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, HomeMVPContractor.View {

    @Inject HomeMVPContractor.Presenter presenter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.resturants_list) RecyclerView recyclerView;

    private HomeComponent component;
    private RestaurantRecyclerViewAdapter restaurantRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);

        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        initView();

        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
    }

    private void initView() {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        restaurantRecyclerViewAdapter = new RestaurantRecyclerViewAdapter();
        restaurantRecyclerViewAdapter.setOnFavioriteClickListener(new OnFavoriteClickListerImpl(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(restaurantRecyclerViewAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshData());
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_login) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private HomeComponent getComponent() {
        if (this.component == null) {
            this.component = this.buildComponent();
            if (this.component == null) {
                throw new IllegalArgumentException("buildComponent() returns null, getComponent() must not be called if buildComponent() returns null");
            }
        }

        return this.component;
    }

    private HomeComponent buildComponent() {
        return DaggerHomeComponent.builder()
            .applicationComponent(((DoorDashApplication) getApplication()).getApplicationComponent())
            .homeModule(new HomeModule())
            .apiModule(new ApiModule())
            .build();
    }

    @Override
    public void showRefreshing() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showRestaurants(List<RestaurantViewModel> restaurants) {
        restaurantRecyclerViewAdapter.setRestaurants(restaurants);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError() {
        Toast.makeText(this, "An error occurred fetching restaurants", Toast.LENGTH_LONG).show();
        swipeRefreshLayout.setRefreshing(false);
        restaurantRecyclerViewAdapter.clearRestaurants();
    }

    @Override
    public void updateFavorite(RestaurantViewModel restaurant, int position) {
        restaurantRecyclerViewAdapter.setRestaurant(restaurant, position);
    }

    private static class OnFavoriteClickListerImpl implements RestaurantRecyclerViewAdapter.OnFavoriteClickListener {
        private WeakReference<HomeActivity> homeActivityWeakReference;

        public OnFavoriteClickListerImpl(HomeActivity homeActivity) {
            this.homeActivityWeakReference = new WeakReference<>(homeActivity);
        }

        @Override
        public void onClick(int position) {
            final HomeActivity homeActivity = homeActivityWeakReference.get();
            if (homeActivity != null) {
                homeActivity.presenter.onClickFavorite(position);
            }
        }
    }
}
