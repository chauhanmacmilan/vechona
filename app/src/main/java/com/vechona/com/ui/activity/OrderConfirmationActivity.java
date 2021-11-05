package com.vechona.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.vechona.app.BuildConfig;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.CreateOrderResponse;
import com.vechona.com.data.remote.apiResponse.OrderDetailsResponse;
import com.vechona.com.ui.adapter.OrderConfirmationItemAdapter;
import com.vechona.com.ui.model.CartItem;
import com.vechona.com.ui.model.CreateOrder;
import com.vechona.com.ui.model.CreateOrderItem;
import com.vechona.com.ui.model.OrderResponse;
import com.vechona.com.ui.model.OrderUpdate;
import com.vechona.com.ui.model.SenderDetailsItem;
import com.vechona.com.ui.model.ShippingAddressItem;
import com.vechona.com.ui.model.UserOrderItem;
import com.vechona.com.ui.utils.BundleParams;
import com.vechona.com.ui.utils.ViewUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderConfirmationActivity extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    @BindView(R.id.tvCartToolbarTotalPrice)
    TextView tvCartToolbarTotalPrice;
    @BindView(R.id.tvCartToolbarTotalItem)
    TextView tvCartToolbarTotalItem;
    @BindView(R.id.tvPaymentMethod)
    TextView tvPaymentMethod;
    @BindView(R.id.tvSupplierName)
    TextView tvSupplierName;
    @BindView(R.id.tvProductCharges)
    TextView tvProductCharges;
    @BindView(R.id.tvShippingCharges)
    TextView tvShippingCharges;
    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.etFinalCustomerAmount)
    EditText etFinalCustomerAmount;
    @BindView(R.id.tvYourMargin)
    TextView tvYourMargin;
    @BindView(R.id.rvOrderConfirmation)
    RecyclerView rvOrderConfirmation;
    @BindView(R.id.llLessAmountError)
    LinearLayout llLessAmountError;
    @BindView(R.id.tvLessAmountError)
    TextView tvLessAmountError;
    @BindView(R.id.llCODCharges)
    LinearLayout llCodCharges;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;
    TextView tvCODCharges;

    private List<CartItem> cartItemList;
    private PreferenceDataHelper preferenceDataHelper;
    private OrderConfirmationItemAdapter itemAdapter;
    private int finalAmount = 0, productPrice = 0, shippingCharge = 0, CODCharge = 0;
    private ApiService apiService;
    private String paymentMethod;

    public static void start(Context context, String paymentMethod, String price) {
        Intent intent = new Intent(context, OrderConfirmationActivity.class);
        intent.putExtra(BundleParams.PAYMENT_METHOD, paymentMethod);
        intent.putExtra(BundleParams.PRICE, price);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        ButterKnife.bind(this);
        tvCODCharges = findViewById(R.id.tvCODCharges);
        initData();
    }

    private void initData() {
        paymentMethod = getIntent().getStringExtra(BundleParams.PAYMENT_METHOD);
        tvPaymentMethod.setText(paymentMethod);
        etFinalCustomerAmount.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!etFinalCustomerAmount.getText().toString().isEmpty()) {
                        finalAmount = Integer.parseInt(etFinalCustomerAmount.getText().toString());
                        int total = productPrice+shippingCharge+CODCharge;
                        if (productPrice != 0 && finalAmount < total) {
                            showLessAmountError(true);
                        } else showLessAmountError(false);
                    }
                    return true;
                }
                return false;
            }
        });

        etFinalCustomerAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showLessAmountError(false);
                if (!s.toString().isEmpty()) {
                    finalAmount = Integer.parseInt(s.toString().trim());
                    int total = productPrice+shippingCharge+CODCharge;
                    setMarginValue((finalAmount - total));
                } else {
                    finalAmount = 0;
                    setMarginValue(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        preferenceDataHelper = PreferenceDataHelper.getInstance(OrderConfirmationActivity.this);
        apiService = RemoteServiceHelper.createService(ApiService.class,
                RemoteServiceHelper.getRetrofitInstance(this));
        itemAdapter = new OrderConfirmationItemAdapter(OrderConfirmationActivity.this);
        rvOrderConfirmation.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rvOrderConfirmation.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvOrderConfirmation.setAdapter(itemAdapter);
        prepareList();
    }

    private void setMarginValue(int value) {
        tvYourMargin.setText(getString(R.string.priceFormat, value));
        if (finalAmount < (productPrice+shippingCharge+CODCharge))
            tvYourMargin.setTextColor(getResources().getColor(R.color.red));
        else tvYourMargin.setTextColor(getResources().getColor(R.color.whatsapp_color));
    }

    private void showLessAmountError(boolean b) {
        if (b) {
            llLessAmountError.setVisibility(View.VISIBLE);
            tvLessAmountError.setText(getString(R.string.error_your_margin, productPrice));
            Animation shake = AnimationUtils.loadAnimation(OrderConfirmationActivity.this, R.anim.shake);
            tvYourMargin.startAnimation(shake);
        } else llLessAmountError.setVisibility(View.GONE);
    }

    private void prepareList() {
        cartItemList = preferenceDataHelper.getCartList();
        if (cartItemList != null && cartItemList.size() > 0) {
            itemAdapter.setCartItemList(cartItemList);
            setUpPrices();
        }
    }

    @OnClick(R.id.buttonPayNow)
    public void onPayNowButtonClick() {
        if (productPrice != 0 && (productPrice+shippingCharge+CODCharge) <= finalAmount)
            startPayment();
        else showLessAmountError(true);
    }

    @OnClick(R.id.ivCartToolbarBack)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setUpPrices() {
        int productCharge = 0;
        int itemCount = 0;

        for (CartItem cartItem : cartItemList) {
            int price;
            if (cartItem.getProduct().getDiscount() > 0) {
                price = cartItem.getProduct().getPrice() - (int) (cartItem.getProduct().getPrice() * cartItem.getProduct().getDiscount()) / 100;
            } else
                price = cartItem.getProduct().getPrice();
            productCharge += price * cartItem.getQuantity();
            itemCount += cartItem.getQuantity();
            //int pri = Integer.valueOf(getIntent().getStringExtra(BundleParams.PRICE));
                    shippingCharge += cartItem.getCategory().getShippingCharges();   /*Old Method To Add Shipping Charges*/
            //shippingCharge += pri;
        }


        if (paymentMethod.equals(getString(R.string.title_radio_online))) {
            CODCharge = 0;
            llCodCharges.setVisibility(View.GONE);
        } else {
            CODCharge = 50;
            llCodCharges.setVisibility(View.VISIBLE);
        }

        //int cartPrice = productCharge + CODCharge;
        //totalPrice = productCharge + CODCharge;
        productPrice = productCharge;

        tvSupplierName.setText(preferenceDataHelper.getCurrentCartSupplier());
        tvProductCharges.setText(getString(R.string.priceFormat, productCharge));
        tvCODCharges.setText(getString(R.string.priceFormat, CODCharge));
        tvShippingCharges.setText(getString(R.string.plusPriceFormat, shippingCharge));
        tvTotalPrice.setText(getString(R.string.priceFormat, productPrice + shippingCharge + CODCharge));
        tvCartToolbarTotalPrice.setText(getString(R.string.toolbarPriceFormat, productPrice + shippingCharge + CODCharge));
        tvCartToolbarTotalItem.setText(getResources().getQuantityString(R.plurals.toolbarItemFormat, itemCount, itemCount));

        //Toast.makeText(this, String.valueOf(totalPrice) + " - " +String.valueOf(shippingCharge) + " - " + String.valueOf(CODCharge), Toast.LENGTH_SHORT).show();
        /*int productCharge = 0;
        int itemCount = 0;

        for (CartItem cartItem : cartItemList) {
            int price;
            if (cartItem.getProduct().getDiscount() > 0) {
                price = cartItem.getProduct().getPrice() - (int) (cartItem.getProduct().getPrice() * cartItem.getProduct().getDiscount()) / 100;
            } else
                price = cartItem.getProduct().getPrice();
            productCharge += price * cartItem.getQuantity();
            itemCount += cartItem.getQuantity();
            shippingCharge += cartItem.getCategory().getShippingCharges();
        }

        int CODCharge;
        if (paymentMethod.equals(getString(R.string.title_radio_online))) {
            CODCharge = 0;
        } else {
            CODCharge = 10;
            llCodCharges.setVisibility(View.VISIBLE);
        }

        int cartPrice = productCharge + CODCharge;
        totalPrice = cartPrice + shippingCharge;

        tvSupplierName.setText(preferenceDataHelper.getCurrentCartSupplier());
        tvProductCharges.setText(getString(R.string.priceFormat, productCharge));
        tvShippingCharges.setText(getString(R.string.plusPriceFormat, shippingCharge));
        tvTotalPrice.setText(getString(R.string.priceFormat, totalPrice));
        tvCartToolbarTotalPrice.setText(getString(R.string.toolbarPriceFormat, totalPrice));
        tvCartToolbarTotalItem.setText(getResources().getQuantityString(R.plurals.toolbarItemFormat, itemCount, itemCount));*/
    }

    private void startPayment() {
        createOrder();
    }

    private void createOrder() {
        //Toast.makeText(this, String.valueOf(totalPrice) + " - " +String.valueOf(shippingCharge) + " - " + String.valueOf(finalAmount), Toast.LENGTH_SHORT).show();
        showLoading();
        CreateOrderItem createOrderItem = new CreateOrderItem();
        ShippingAddressItem shippingAddressItem = preferenceDataHelper.getSelectedShippingAddress();
        SenderDetailsItem senderDetailsItem = preferenceDataHelper.getSelectedSenderDetail();
        if (shippingAddressItem != null) {
            createOrderItem.setUserId(preferenceDataHelper.getUser().getUserID());
            createOrderItem.setSenderMobileNumber(preferenceDataHelper.getUser().getPhoneNumber());
            createOrderItem.setTotalAmount(finalAmount);
            createOrderItem.setCustomerName(shippingAddressItem.getName());
            createOrderItem.setCustomerAddress(shippingAddressItem.getAddress());
            createOrderItem.setCustomerMobileNumber(shippingAddressItem.getPhoneNumber());
            createOrderItem.setPaymentMethod(paymentMethod);
            createOrderItem.setComissionAmount(finalAmount - (productPrice+shippingCharge+CODCharge));
            createOrderItem.setShippingCharges(shippingCharge);
            createOrderItem.setCodCharges(CODCharge);
        }

        if (senderDetailsItem != null)
            createOrderItem.setSenderName(senderDetailsItem.getName());

        CreateOrder createOrder = new CreateOrder(createOrderItem);

        apiService.createOrder(createOrder).enqueue(new Callback<CreateOrderResponse>() {
            @Override
            public void onResponse(Call<CreateOrderResponse> call, Response<CreateOrderResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("success")) {
                    hideLoading();
                    OrderResponse orderResponse = response.body().getOrderResponseData().getOrderResponse();
                    startPaytmPayment(orderResponse);
                }
            }

            @Override
            public void onFailure(Call<CreateOrderResponse> call, Throwable t) {
                hideLoading();
            }
        });
    }

    private void startPaytmPayment(OrderResponse orderResponse) {

        PaytmPGService service = PaytmPGService.getStagingService();
        /*if (BuildConfig.DEBUG)
            service = PaytmPGService.getStagingService();
        else service = PaytmPGService.getProductionService();*/

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", orderResponse.getMid());
        paramMap.put("ORDER_ID", orderResponse.getOrderId());
        paramMap.put("CUST_ID", orderResponse.getCustId());
        //paramMap.put("MOBILE_NO", preferenceDataHelper.getUser().getPhoneNumber());
        //paramMap.put("EMAIL", preferenceDataHelper.getUser().getEmail());
        paramMap.put("CHANNEL_ID", orderResponse.getChannelId());
        paramMap.put("TXN_AMOUNT", orderResponse.getTxnAmount());
        paramMap.put("WEBSITE", orderResponse.getWebsite());
        paramMap.put("INDUSTRY_TYPE_ID", orderResponse.getIndustryTypeId());
        paramMap.put("CALLBACK_URL", orderResponse.getCallbackUrl());
        paramMap.put("CHECKSUMHASH", orderResponse.getCheckSumHash());
        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) paramMap);

        service.initialize(Order, null);
        service.startPaymentTransaction(this, true, true, this);

    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {
        showLoading();
        String[] s = inResponse.keySet().toArray(new String[0]);

        final OrderUpdate orderUpdate = new OrderUpdate();

        orderUpdate.setUserId(preferenceDataHelper.getUser().getUserID());
        orderUpdate.setStatus(inResponse.getString(s[0]));
        orderUpdate.setCheckSumHash(inResponse.getString(s[1]));
        orderUpdate.setBankName(inResponse.getString(s[2]));
        orderUpdate.setOrderId(inResponse.getString(s[3]));
        orderUpdate.setTxnAmount(inResponse.getString(s[4]));
        orderUpdate.setTxnDate(inResponse.getString(s[5]));
        orderUpdate.setMid(inResponse.getString(s[6]));
        orderUpdate.setTxnId(inResponse.getString(s[7]));
        orderUpdate.setRepsCode(inResponse.getString(s[8]));
        orderUpdate.setPaymentMode(inResponse.getString(s[9]));
        orderUpdate.setBankTxnId(inResponse.getString(s[10]));
        orderUpdate.setCurrency(inResponse.getString(s[11]));
        orderUpdate.setGateWayName(inResponse.getString(s[12]));
        orderUpdate.setRepsMsg(inResponse.getString(s[13]));

        apiService.updateOrder(orderUpdate).enqueue(new Callback<OrderDetailsResponse>() {
            @Override
            public void onResponse(Call<OrderDetailsResponse> call, Response<OrderDetailsResponse> response) {
                if (response.isSuccessful() && response.body().getData() != null && response.body().getStatus().equals("success")) {
                    hideLoading();
                    UserOrderItem orderDetails = response.body().getData().getOrderDetails();
                    emptyCart(orderDetails);
                }
            }

            @Override
            public void onFailure(Call<OrderDetailsResponse> call, Throwable t) {
                hideLoading();
            }
        });
    }

    private void emptyCart(UserOrderItem orderDetails) {
        preferenceDataHelper.clearCart();
        ViewUtils.showToast(OrderConfirmationActivity.this, getString(R.string.msg_order_placed_successfully));
        HomeActivity.start(OrderConfirmationActivity.this);
        OrderDetailActivity.start(OrderConfirmationActivity.this, orderDetails);
    }

    @Override
    public void someUIErrorOccurred(String inErrorMessage) {
        Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String inErrorMessage) {
        Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
        Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
        Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void showLoading() {
        progressWheel.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressWheel.setVisibility(View.GONE);
    }
}
