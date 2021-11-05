package com.vechona.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.UserOrderListResponse;
import com.vechona.com.ui.model.UserOrderItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPaymentsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvOnHold)
    TextView tvOnHold;

    @BindView(R.id.tvCleared)
    TextView tvCleared;

    @BindView(R.id.tvUncleared)
    TextView tvUnCleared;

    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private List<UserOrderItem> userOrderItemList;

    public static void start(Context context) {
        Intent intent = new Intent(context, MyPaymentsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_payments);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.my_payment));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        ApiService apiService = RemoteServiceHelper.createService(ApiService.class, RemoteServiceHelper.getRetrofitInstance(this));
        progressWheel.setVisibility(View.VISIBLE);
        apiService.getUserOrderList(PreferenceDataHelper.getInstance(this)
                .getUser()
                .getUserID())
                .enqueue(new Callback<UserOrderListResponse>() {
                    @Override
                    public void onResponse(Call<UserOrderListResponse> call, Response<UserOrderListResponse> response) {
                        if (response.isSuccessful() && response.body() != null
                                && response.body().getStatus().equals("success")) {
                            progressWheel.setVisibility(View.GONE);
                            userOrderItemList = response.body().getUserOrderListData().getUserOrderItems();
                            if (userOrderItemList != null && userOrderItemList.size() > 0) {
                                int th = 0, tc = 0, tuc = 0;
                                for (UserOrderItem userOrderItem : userOrderItemList) {
                                    if ("ONHOLD".equalsIgnoreCase(userOrderItem.getComissionStatus()))
                                        th += Float.valueOf(userOrderItem.getComissionAmount());
                                    else if ("CLEARED".equalsIgnoreCase(userOrderItem.getComissionStatus()))
                                        tc += Float.valueOf(userOrderItem.getComissionAmount());
                                    else
                                        tuc += Float.valueOf(userOrderItem.getComissionAmount());
                                }
                                setText(th, tc, tuc);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserOrderListResponse> call, Throwable t) {
                        progressWheel.setVisibility(View.GONE);
                    }
                });
    }

    private void setText(int th, int tc, int tuc) {
        tvOnHold.setText(getString(R.string.priceFormat, th));
        tvCleared.setText(getString(R.string.priceFormat, tc));
        tvUnCleared.setText(getString(R.string.priceFormat, tuc));
    }
}
