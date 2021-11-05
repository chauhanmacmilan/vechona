package com.vechona.com.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.vechona.app.R;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.ReviewResponse;
import com.vechona.com.data.remote.apiResponse.ReviewResponseData;
import com.vechona.com.data.remote.apiResponse.UserOrderDetailsData;
import com.vechona.com.data.remote.apiResponse.UserOrderDetailsResponse;
import com.vechona.com.ui.adapter.OrderDetailsItemAdapter;
import com.vechona.com.ui.fragment.FeedBackDialog;
import com.vechona.com.ui.interfaces.OnOrderDetailsButtonClick;
import com.vechona.com.ui.model.OrderDetails;
import com.vechona.com.ui.model.Review;
import com.vechona.com.ui.model.UserOrderItem;
import com.vechona.com.ui.utils.BundleParams;
import com.vechona.com.ui.utils.DateFormatUtils;
import com.vechona.com.ui.utils.StringUtils;
import com.vechona.com.ui.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity implements FeedBackDialog.FeedBackDialogListner {

    @BindView(R.id.tvOrderId)
    TextView tvOrderId;
    @BindView(R.id.ivCopy)
    AppCompatImageView ivCopy;
    @BindView(R.id.tvItemNumber)
    TextView tvItemNumber;
    @BindView(R.id.tvProductCharges)
    TextView tvProductCharges;
    @BindView(R.id.tvShippingCharges)
    TextView tvShippingCharges;
    @BindView(R.id.tvCODCharges)
    TextView tvCODCharges;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.tvCODAmount)
    TextView tvCODAmount;
    @BindView(R.id.rvProducts)
    RecyclerView rvProducts;
    @BindView(R.id.tvSupplierName)
    TextView tvSupplierName;
    @BindView(R.id.tvPaymentMethod)
    TextView tvPaymentMethod;
    @BindView(R.id.tvPlacedOn)
    TextView tvPlacedOn;
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @BindView(R.id.tvCustomerAddress)
    TextView tvCustomerAddress;
    @BindView(R.id.tvCustomerNumber)
    TextView tvCustomerNumber;
    @BindView(R.id.tvSenderName)
    TextView tvSenderName;
    @BindView(R.id.tvSenderNumber)
    TextView tvSenderNumber;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    TextView tvProductCommision;

    private ApiService apiService;
    private UserOrderItem userOrderItem;
    private OrderDetailsItemAdapter orderItemAdapter;
    private String userOrderId;


    public static void start(Context context, UserOrderItem userOrderItem) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(BundleParams.USER_ORDER_ITEM, userOrderItem);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        userOrderItem = getIntent().getParcelableExtra(BundleParams.USER_ORDER_ITEM);
        userOrderId = getIntent().getStringExtra(BundleParams.USER_ORDER_ID);
        tvProductCommision = findViewById(R.id.tvProductCommision);
        initView();
        apiService = RemoteServiceHelper.createService(ApiService.class,
                RemoteServiceHelper.getRetrofitInstance(this));
        if (userOrderId != null) {
            initData();
        } else
            setOrderDetails();
    }

    @OnClick(R.id.ivCopy)
    public void onCopyClick() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("orderId", tvOrderId.getText().toString().trim());
        clipboard.setPrimaryClip(clip);
        ViewUtils.showToast(OrderDetailActivity.this, getString(R.string.msg_orderid_copied));
    }

    private void initView() {
        orderItemAdapter = new OrderDetailsItemAdapter(new OnOrderDetailsButtonClick() {
            @Override
            public void onTrackButtonClick(int position) {

            }

            @Override
            public void onCancelButtonClick(int position) {
                CancelOrderActivity.start(OrderDetailActivity.this,
                        userOrderItem.getProductList().get(position), userOrderItem.getOrderId());
            }

            @Override
            public void onFeedbackButtonClick(int position) {
                String tag = "FeedBackDialog";
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
                if (prev != null)
                    return;
                FeedBackDialog dialog = FeedBackDialog.newInstance(position);
                dialog.show(ft, tag);
            }
        }, OrderDetailActivity.this);

        rvProducts.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rvProducts.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this,
                LinearLayoutManager.VERTICAL, false));
        rvProducts.setAdapter(orderItemAdapter);
    }

    private void initData() {
        showLoading();
        apiService.getOrderDetails(new OrderDetails(userOrderId))
                .enqueue(new Callback<UserOrderDetailsResponse>() {
                    @Override
                    public void onResponse(Call<UserOrderDetailsResponse> call, Response<UserOrderDetailsResponse> response) {
                        UserOrderDetailsData userOrderDetailsData = response.body().getUserOrderDetailsData();
                        if (response.isSuccessful() && userOrderDetailsData != null) {
                            hideLoading();
                            userOrderItem = userOrderDetailsData.getUserOrderItem();
                            setOrderDetails();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserOrderDetailsResponse> call, Throwable t) {
                        hideLoading();
                    }
                });
    }

    private void setOrderDetails() {
        tvOrderId.setText(getString(R.string.title_order_id, userOrderItem.getOrderId()));
        tvItemNumber.setText(getString(R.string.title_no_of_item, userOrderItem.getTotalItems()));
        int totalminus = Integer.parseInt(userOrderItem.getComissionAmount()) + Integer.parseInt(userOrderItem.getShippingCharges()) + Integer.parseInt(userOrderItem.getCodCharges());
        int productPrice = Integer.parseInt(userOrderItem.getTotalAmount()) - totalminus;
        tvProductCharges.setText(getString(R.string.priceFormatString, String.valueOf(productPrice)));
        tvProductCommision.setText(getString(R.string.priceFormatString, userOrderItem.getComissionAmount()));
        tvShippingCharges.setText(getString(R.string.plusPriceFormatString, userOrderItem.getShippingCharges()));
        tvCODCharges.setText(getString(R.string.plusPriceFormatString, userOrderItem.getCodCharges()));
        tvTotal.setText(getString(R.string.priceFormatString, userOrderItem.getTotalAmount()));
        tvCODAmount.setText(getString(R.string.priceFormatString, userOrderItem.getCodAmountCollection()));
        tvSupplierName.setText(StringUtils.camelCase(userOrderItem.getProductList().get(0).getVender()));
        tvPaymentMethod.setText(userOrderItem.getPaymentMethod());
        tvPlacedOn.setText(DateFormatUtils.getDateWithYear(userOrderItem.getOrderCreationDate()));
        tvCustomerName.setText(StringUtils.camelCase(userOrderItem.getCustomerName()));
        tvCustomerAddress.setText(userOrderItem.getCustomerAddress());
        tvCustomerNumber.setText(userOrderItem.getCustomerMobileNumber());
        tvSenderName.setText(StringUtils.camelCase(userOrderItem.getSenderName()));
        tvSenderNumber.setText(userOrderItem.getSenderMobileNumber());
        if (userOrderItem.getProductList() != null) {
            orderItemAdapter.setProductList(userOrderItem.getProductList(), userOrderItem.getOrderStatus());
        }
    }

    @OnClick(R.id.ivCartToolbarBack)
    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            HomeActivity.start(this);
        } else
            super.onBackPressed();
    }

    @Override
    public void onSubmitButtonClick(int position, int rating, String comments) {
        showLoading();
        apiService.addProductReview(new Review(userOrderItem.getUserId(), userOrderItem.getOrderId(), userOrderItem.getProductList().get(position).getProductId(), rating, comments))
                .enqueue(new Callback<ReviewResponse>() {
                    @Override
                    public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                        ReviewResponseData reviewResponseData = response.body().getReviewResponseData();
                        if (response.isSuccessful() && reviewResponseData != null) {
                            hideLoading();
                            ViewUtils.showToast(OrderDetailActivity.this, getString(R.string.msg_review_added));
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewResponse> call, Throwable t) {
                        hideLoading();
                    }
                });
    }

    public void showLoading() {
        progressWheel.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressWheel.setVisibility(View.GONE);
    }
}
