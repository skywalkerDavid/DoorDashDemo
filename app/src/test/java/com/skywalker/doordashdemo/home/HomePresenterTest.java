package com.skywalker.doordashdemo.home;

import com.skywalker.doordashdemo.api.Restaurant;
import com.skywalker.doordashdemo.api.RestaurantApiClient;
import com.skywalker.doordashdemo.api.Restaurants;
import java.util.ArrayList;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.MockType.NICE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static rx.Observable.just;

public class HomePresenterTest extends EasyMockSupport {
    @Rule public final EasyMockRule easyMockRule = new EasyMockRule(this);

    @Mock(type = NICE) RestaurantApiClient apiClient;
    @Mock(type = NICE) HomeMVPContractor.View view;

    private HomePresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new HomePresenter(apiClient);
    }

    @Test
    public void attachViewTest() throws Exception {
        // Given
        view.showRefreshing();
        expectLastCall().once();
        replayAll();

        presenter.isRefreshing = true;

        // When
        presenter.attachView(view);

        // Then
        assertThat(presenter.view, is(view));
        verifyAll();
    }

    @Test
    public void detachViewTest() throws Exception {
        presenter.view = view;

        presenter.detachView();

        assertThat(presenter.view, is(nullValue()));
    }

    @Test
    public void onClickFavoriteTest() throws Exception {
        view.updateFavorite(anyObject(), eq(0));
        expectLastCall().once();

        replayAll();

        presenter.restaurantViewModels = new ArrayList<>();
        presenter.restaurantViewModels.add(new RestaurantViewModel());
        presenter.view = view;
        presenter.onClickFavorite(0);

        verifyAll();
    }

    @Test
    public void refreshDataObservableTest() throws Exception {
        Restaurant restaurant = new Restaurant();
        Restaurants expectedRestaurants = new Restaurants();
        expectedRestaurants.add(restaurant);

        final Observable<Restaurants> observable = just(expectedRestaurants).cache();

        TestSubscriber<Restaurants> testSubscriber = new TestSubscriber<>();
        observable.subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(expectedRestaurants);
        testSubscriber.assertCompleted();
    }
}
