package com.vechona.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vechona.app.R;
import com.vechona.com.ui.adapter.ViewPagerAdapter;
import com.vechona.com.ui.fragment.ShareCatalogsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareCatalogsActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager_SharedCatalog)
    ViewPager viewPager;

    public static void start(Context context) {
        Intent intent = new Intent(context, ShareCatalogsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_catalog);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbar.setTitle(R.string.title_my_catalogs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.wishlist)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.shared)));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setupViewPager();
    }

    private void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        ShareCatalogsFragment shareCatalogsFragment = ShareCatalogsFragment.newInstance(true);
        viewPagerAdapter.addFragment(shareCatalogsFragment);

        ShareCatalogsFragment shareCatalogsFragment1 = ShareCatalogsFragment.newInstance(false);
        viewPagerAdapter.addFragment(shareCatalogsFragment1);

        viewPager.setAdapter(viewPagerAdapter);
    }


}
