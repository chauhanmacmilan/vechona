package com.vechona.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.vechona.app.R;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.OrderDetailsResponse;
import com.vechona.com.ui.model.CancelOrder;
import com.vechona.com.ui.model.Product;
import com.vechona.com.ui.utils.BundleParams;
import com.vechona.com.ui.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelOrderActivity extends AppCompatActivity {

    @BindView(R.id.ivProductImage)
    AppCompatImageView ivProductImage;
    @BindView(R.id.tvProductName)
    TextView tvProductName;
    @BindView(R.id.tvProductSize)
    TextView tvProductSize;
    @BindView(R.id.tvProductQnty)
    TextView tvProductQnty;
    @BindView(R.id.spinnerReasons)
    Spinner spinnerReasons;
    @BindView(R.id.etTellUsMore)
    EditText etTellUsMore;
    @BindView(R.id.buttonCancelProduct)
    Button buttonCancelProduct;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    public static void start(Context context, Product product, String orderId) {
        Intent intent = new Intent(context, CancelOrderActivity.class);
        intent.putExtra(BundleParams.PRODUCT, product);
        intent.putExtra(BundleParams.ORDER_ID, orderId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick(R.id.ivCartToolbarBack)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.buttonCancelProduct)
    public void onCancelProductButtonClick() {
        progressWheel.setVisibility(View.VISIBLE);
        RemoteServiceHelper.createService(ApiService.class,
                RemoteServiceHelper.getRetrofitInstance(this))
                .orderCancel(new CancelOrder(getIntent().getStringExtra(BundleParams.ORDER_ID),
                        spinnerReasons.getSelectedItem().toString()))
                .enqueue(new Callback<OrderDetailsResponse>() {
                    @Override
                    public void onResponse(Call<OrderDetailsResponse> call, Response<OrderDetailsResponse> response) {
                        progressWheel.setVisibility(View.GONE);
                        ViewUtils.showToast(CancelOrderActivity.this,
                                getString(R.string.msg_order_canceled_successfully));
                        HomeActivity.start(CancelOrderActivity.this);
                        OrderDetailActivity.start(CancelOrderActivity.this,
                                response.body().getData().getOrderDetails());
                    }

                    @Override
                    public void onFailure(Call<OrderDetailsResponse> call, Throwable t) {
                        progressWheel.setVisibility(View.GONE);
                    }
                });
    }

    private void initView() {
        Product product = getIntent().getParcelableExtra(BundleParams.PRODUCT);
        Glide.with(CancelOrderActivity.this).load(product.getProductsImageUrls().get(0).getImageUrl()).into(ivProductImage);
        tvProductName.setText(product.getProductName());
        tvProductSize.setText(product.getSize());
        tvProductQnty.setText(product.getQuantity());

        List<String> reasonsList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.orderCancelReasons)));
        spinnerReasons.setAdapter(new ArrayAdapter<String>(CancelOrderActivity.this, R.layout.item_reason_spinner, reasonsList));

        spinnerReasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    buttonCancelProduct.setEnabled(true);
                    buttonCancelProduct.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    buttonCancelProduct.setEnabled(false);
                    buttonCancelProduct.setBackgroundColor(getResources().getColor(R.color.grey_50));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}

