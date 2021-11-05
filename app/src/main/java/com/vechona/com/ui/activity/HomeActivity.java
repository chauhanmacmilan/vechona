package com.vechona.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.GetCartResponse;
import com.vechona.com.data.remote.apiResponse.SettingResponse;
import com.vechona.com.data.remote.apiResponse.SettingResponseData;
import com.vechona.com.ui.adapter.ViewPagerAdapter;
import com.vechona.com.ui.fragment.CollectionsFragment;
import com.vechona.com.ui.fragment.DashboardFragment;
import com.vechona.com.ui.fragment.OrderFragment;
import com.vechona.com.ui.fragment.ProfileFragment;
import com.vechona.com.ui.model.Setting;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    public static void start(Context context) {
        Intent starter = new Intent(context, HomeActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.dashboard:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.collection:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.order:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.account:
                                viewPager.setCurrentItem(3);
                                break;
                        }
                        return true;
                    }
                });
        setupViewPager(viewPager);

        initData();
    }

    private void initData() {
        ApiService apiService =
                RemoteServiceHelper.createService(ApiService.class,
                        RemoteServiceHelper.getRetrofitInstance(HomeActivity.this));
        final PreferenceDataHelper preferenceDataHelper = PreferenceDataHelper.getInstance(this);

        apiService.getUserCart(preferenceDataHelper.getUser().getUserID()).enqueue(new Callback<GetCartResponse>() {
            @Override
            public void onResponse(Call<GetCartResponse> call, Response<GetCartResponse> response) {
                if (response.isSuccessful() && response.body().getCartResponseData() != null
                        && response.body().getStatus().equals("success")) {
                    preferenceDataHelper
                            .saveCartList(response.body().getCartResponseData().getCartItemList());
                }
            }

            @Override
            public void onFailure(Call<GetCartResponse> call, Throwable t) {

            }
        });

        apiService.getSetting().enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                SettingResponseData settingResponseData = response.body().getSettingResponseData();
                if (response.isSuccessful() && settingResponseData != null) {
                    List<Setting> settings = settingResponseData.getSettings();
                    for (Setting setting :
                            settings) {
                        if (setting.getName().equalsIgnoreCase("cod_enable")) {
                            preferenceDataHelper.setCODEnable(setting.getValue().equals("true") ? true : false);
                        } /*else if (setting.getName().equalsIgnoreCase("return_policy_period")) {
                            preferenceDataHelper.setReturnPolicyPeriod(setting.getValue());
                        }*/
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingResponse> call, Throwable t) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        DashboardFragment dashboardFragment = new DashboardFragment();
        CollectionsFragment collectionsFragment = new CollectionsFragment();
        OrderFragment orderFragment = OrderFragment.newInstance();
        ProfileFragment profileFragment = new ProfileFragment();
        adapter.addFragment(dashboardFragment);
        adapter.addFragment(collectionsFragment);
        adapter.addFragment(orderFragment);
        adapter.addFragment(profileFragment);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
    }

    @Override
    public void onBackPressed() {
        if (bottomNavigationView.getSelectedItemId() == R.id.order) {
            bottomNavigationView.setSelectedItemId(R.id.dashboard);
        } else super.onBackPressed();
    }
}
