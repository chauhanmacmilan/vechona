package com.vechona.com.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.DeliveryPincodeResponse;
import com.vechona.com.ui.adapter.ShippingAddressItemAdapter;
import com.vechona.com.ui.interfaces.RecyclerViewItemClickListener;
import com.vechona.com.ui.model.GetPincodeAvailablePrice;
import com.vechona.com.ui.model.PincodeCheckModel;
import com.vechona.com.ui.model.ShippingAddressItem;
import com.vechona.com.ui.utils.BundleParams;
import com.vechona.com.ui.utils.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingAddressActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1000;

    @BindView(R.id.rvShippingAddress)
    RecyclerView rvShippingAddress;

    @BindView(R.id.btnProceed)
    Button btnProceed;

    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private List<ShippingAddressItem> shippingAddressItems;
    private ShippingAddressItemAdapter addressItemAdapter;
    private PreferenceDataHelper preferenceDataHelper;

    public String payMethod = "", pincode = "";
    public int weightItem = 0;

    public static void start(Context context, String paymentMethod, int weight) {
        Intent intent = new Intent(context, ShippingAddressActivity.class);
        intent.putExtra(BundleParams.PAYMENT_METHOD, paymentMethod);
        intent.putExtra("weight", weight);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);
        ButterKnife.bind(this);
        if(getIntent() != null) {
            payMethod = getIntent().getStringExtra(BundleParams.PAYMENT_METHOD);
            weightItem = getIntent().getIntExtra("weight", 0);
        }
        initView();
        initData();
    }

    private void initView() {

        addressItemAdapter = new ShippingAddressItemAdapter(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ShippingAddressActivity.this, AddShippingAddressActivity.class);
                intent.putExtra(BundleParams.IS_EDIT, true);
                intent.putExtra(BundleParams.POSITION, position);
                intent.putExtra(BundleParams.SHIPPING_ADDRESS, shippingAddressItems.get(position));
                startActivityForResult(intent, REQUEST_CODE);
            }

            @Override
            public void onRadioClick(int adapterPosition, int position) {
                shippingAddressItems.get(adapterPosition).setSelected(true);
                if (position != adapterPosition)
                    shippingAddressItems.get(position).setSelected(false);
                preferenceDataHelper.saveShippingAddressList(shippingAddressItems);
                addressItemAdapter.notifyItemChanged(adapterPosition);
                addressItemAdapter.notifyItemChanged(position);
            }
        });
        rvShippingAddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvShippingAddress.setAdapter(addressItemAdapter);
    }

    private void initData() {
        preferenceDataHelper = PreferenceDataHelper.getInstance(ShippingAddressActivity.this);
        prepareList();
    }

    private void prepareList() {
        shippingAddressItems = preferenceDataHelper.getShippingAddressList();
        if (shippingAddressItems != null && shippingAddressItems.size() > 0)
            addressItemAdapter.setAddressItemList(shippingAddressItems);
    }

    @OnClick(R.id.ivCartToolbarBack)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.llAddNew)
    public void onAddNewClick() {
        Intent intent = new Intent(ShippingAddressActivity.this, AddShippingAddressActivity.class);
        intent.putExtra(BundleParams.IS_EDIT, false);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra(BundleParams.IS_EDIT) && data.getBooleanExtra(BundleParams.IS_EDIT, false)) {
                int position = data.getIntExtra(BundleParams.POSITION, -1);
                ShippingAddressItem addressItem = data.getParcelableExtra(BundleParams.SHIPPING_ADDRESS);
                if (position != -1 && addressItem != null) {
                    shippingAddressItems.remove(position);
                    shippingAddressItems.add(position, addressItem);
                    preferenceDataHelper.saveShippingAddressList(shippingAddressItems);
                }
            }
        }
        prepareList();
    }

    @OnClick(R.id.btnProceed)
    public void onProceedButtonClick() {
        if(shippingAddressItems != null && shippingAddressItems.size() > 0 && preferenceDataHelper.getSelectedShippingAddress() != null) {
            pincode = preferenceDataHelper.getSelectedShippingAddress().getAddress().split(",")[5].trim();
            Toast.makeText(this, payMethod+"++"+String.valueOf(weightItem)+"++"+pincode, Toast.LENGTH_SHORT).show();
            if (shippingAddressItems != null && preferenceDataHelper.getSelectedShippingAddress() != null) {
                showLoading();
                //final String paymentMethod = getIntent().getStringExtra(BundleParams.PAYMENT_METHOD);
                //SenderDetailsActivity.start(ShippingAddressActivity.this, payMethod);
                ApiService apiService = RemoteServiceHelper.createService(ApiService.class, RemoteServiceHelper.getRetrofitInstance(this));
                PincodeCheckModel pincodeCheckModel;
                if(payMethod.equals(getString(R.string.title_radio_online))) {
                    pincodeCheckModel = new PincodeCheckModel(666, weightItem, payMethod);
                    SenderDetailsActivity.start(ShippingAddressActivity.this, payMethod, "0");
                } else {
                    pincodeCheckModel = new PincodeCheckModel(Integer.valueOf(pincode), weightItem, payMethod);
                    apiService.checkAvailabilityNew(pincodeCheckModel).enqueue(new Callback<GetPincodeAvailablePrice>() {
                        @Override
                        public void onResponse(Call<GetPincodeAvailablePrice> call, Response<GetPincodeAvailablePrice> response) {
                            if(response.isSuccessful() && response.body().status.equals("success")) {
                                hideLoading();
                                Toast.makeText(ShippingAddressActivity.this, String.valueOf(response.body().data.getPRICE()), Toast.LENGTH_SHORT).show();
                                SenderDetailsActivity.start(ShippingAddressActivity.this, payMethod, String.valueOf(response.body().data.getPRICE()));
                            } else {
                                hideLoading();
                                ViewUtils.showToast(ShippingAddressActivity.this, getString(R.string.address_unserviceable));
                            }
                        }

                        @Override
                        public void onFailure(Call<GetPincodeAvailablePrice> call, Throwable t) {
                            hideLoading();
                            Toast.makeText(ShippingAddressActivity.this, "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            /*if (paymentMethod.equals(getString(R.string.title_radio_online))) {
                apiService.checkAvailability(pincode)
                        .enqueue(new Callback<DeliveryPincodeResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<DeliveryPincodeResponse> call, @NonNull Response<DeliveryPincodeResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    hideLoading();
                                    boolean is_serviceable = response.body().isData();
                                    if (is_serviceable)
                                        SenderDetailsActivity.start(ShippingAddressActivity.this, paymentMethod);
                                    else
                                        ViewUtils.showToast(ShippingAddressActivity.this, getString(R.string.address_unserviceable));
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<DeliveryPincodeResponse> call, @NonNull Throwable t) {
                                hideLoading();
                            }
                        });
            } else {
                apiService.checkCODAvailability(pincode)
                        .enqueue(new Callback<DeliveryPincodeResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<DeliveryPincodeResponse> call, @NonNull Response<DeliveryPincodeResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    hideLoading();
                                    boolean is_serviceable = response.body().isData();
                                    if (is_serviceable)
                                        SenderDetailsActivity.start(ShippingAddressActivity.this, paymentMethod);
                                    else
                                        ViewUtils.showToast(ShippingAddressActivity.this, getString(R.string.address_unserviceable));
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<DeliveryPincodeResponse> call, @NonNull Throwable t) {
                                hideLoading();
                            }
                        });
            }*/
            } else {
                if (shippingAddressItems != null && shippingAddressItems.size() > 0)
                    ViewUtils.showToast(ShippingAddressActivity.this, getString(R.string.alert_select_item));
                else
                    ViewUtils.showToast(ShippingAddressActivity.this, getString(R.string.alert_add_shipping_address));
            }
        } else {
            Toast.makeText(this, "Please Select Address.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showLoading() {
        progressWheel.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressWheel.setVisibility(View.GONE);
    }
}