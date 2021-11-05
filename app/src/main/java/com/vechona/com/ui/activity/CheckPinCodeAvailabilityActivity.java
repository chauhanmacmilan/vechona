package com.vechona.com.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vechona.app.R;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.DeliveryPincodeResponse;
import com.vechona.com.ui.utils.BundleParams;
import com.vechona.com.ui.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckPinCodeAvailabilityActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.input_layout_pincode1)
    TextInputLayout inputLayoutPincode1;

    @BindView(R.id.etPincode)
    EditText etPincode;

    @BindView(R.id.btnCheck)
    Button btnCheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pincode_availabilty);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.check_availability);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validation(s.toString()))
                    btnCheck.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                else
                    btnCheck.setBackgroundColor(getResources().getColor(R.color.grey_40));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.btnCheck)
    public void onClickCheck() {
        final String pinCode = etPincode.getText().toString();
        if (validation(pinCode)) {
            ApiService apiService = RemoteServiceHelper
                    .createService(ApiService.class,
                            RemoteServiceHelper.getRetrofitInstance(this));

            apiService.checkAvailability(pinCode)
                    .enqueue(new Callback<DeliveryPincodeResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<DeliveryPincodeResponse> call, @NonNull Response<DeliveryPincodeResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Intent intent = new Intent();
                                intent.putExtra(BundleParams.IS_AVAILABLE, response.body().isData());
                                intent.putExtra(BundleParams.PINCODE, pinCode);
                                setResult(2, intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<DeliveryPincodeResponse> call, @NonNull Throwable t) {
                            ViewUtils.showToast(CheckPinCodeAvailabilityActivity.this, getString(R.string.error_connection_failed));
                        }
                    });

        }
    }

    private boolean validation(String pinCode) {
        if (pinCode.isEmpty()) {
            inputLayoutPincode1.setError(getString(R.string.error_required));
            return false;
        } else if (pinCode.length() != 6) {
            inputLayoutPincode1.setError(getString(R.string.error_pincode_length));
            return false;
        } else {
            inputLayoutPincode1.setError(null);
        }

        return true;
    }
}