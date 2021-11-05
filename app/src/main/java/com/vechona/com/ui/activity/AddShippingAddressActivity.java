package com.vechona.com.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.ui.model.ShippingAddressItem;
import com.vechona.com.ui.utils.BundleParams;
import com.vechona.com.ui.utils.ValidationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddShippingAddressActivity extends AppCompatActivity {

    @BindView(R.id.input_layout_name)
    TextInputLayout input_layout_name;
    @BindView(R.id.input_layout_phone_number)
    TextInputLayout input_layout_phone_number;
    @BindView(R.id.input_layout_house_no)
    TextInputLayout input_layout_house_no;
    @BindView(R.id.input_layout_street)
    TextInputLayout input_layout_street;
    @BindView(R.id.input_layout_city)
    TextInputLayout input_layout_city;
    @BindView(R.id.input_layout_landmark)
    TextInputLayout input_layout_landmark;
    @BindView(R.id.input_layout_state)
    TextInputLayout input_layout_state;
    @BindView(R.id.input_layout_pincode)
    TextInputLayout input_layout_pincode;

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.etHouseNo)
    EditText etHouseNo;
    @BindView(R.id.etStreet)
    EditText etStreet;
    @BindView(R.id.etCity)
    EditText etCity;
    @BindView(R.id.etLandmark)
    EditText etLandmark;
    @BindView(R.id.etState)
    EditText etState;
    @BindView(R.id.etPincode)
    EditText etPincode;

    private ShippingAddressItem shippingAddressItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_shipping_address);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (getIntent().hasExtra(BundleParams.IS_EDIT)
                && getIntent().getBooleanExtra(BundleParams.IS_EDIT, false)
                && getIntent().hasExtra(BundleParams.SHIPPING_ADDRESS)) {
            shippingAddressItem = getIntent().getParcelableExtra(BundleParams.SHIPPING_ADDRESS);
            setUpEditView();
        }
    }

    private void setUpEditView() {
        etName.setText(shippingAddressItem.getName());
        etPhoneNumber.setText(shippingAddressItem.getPhoneNumber().substring(3));
        String address = shippingAddressItem.getAddress();
        String[] split = address.split(",");
        etHouseNo.setText(split[0]);
        etStreet.setText(split[1]);
        etCity.setText(split[2]);
        etLandmark.setText(split[3]);
        etState.setText(split[4]);
        etPincode.setText(split[5]);
    }

    @OnClick(R.id.btnSave)
    public void onSaveButtonClick() {
        if (validation()) {
            saveAddress();
        }
    }

    @OnClick(R.id.ivCartToolbarBack)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void saveAddress() {
        if (!(getIntent().getBooleanExtra(BundleParams.IS_EDIT, false)))
            PreferenceDataHelper.getInstance(AddShippingAddressActivity.this).addShippingAddress(shippingAddressItem);
        Intent intent = new Intent();
        intent.putExtra(BundleParams.IS_EDIT, getIntent().getBooleanExtra(BundleParams.IS_EDIT, false));
        intent.putExtra(BundleParams.POSITION, getIntent().getIntExtra(BundleParams.POSITION, -1));
        intent.putExtra(BundleParams.SHIPPING_ADDRESS, shippingAddressItem);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private boolean validation() {

        String name = etName.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String houseNo = etHouseNo.getText().toString().trim();
        String street = etStreet.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String landmark = etLandmark.getText().toString().trim();
        String state = etState.getText().toString().trim();
        String pincode = etPincode.getText().toString().trim();

        if (name.isEmpty()) {
            input_layout_name.setError(getString(R.string.error_required));
            return false;
        } else input_layout_name.setError(null);

        if (phoneNumber.isEmpty()) {
            input_layout_phone_number.setError(getString(R.string.error_required));
            return false;
        } else if (!ValidationUtils.isValidPhoneNumber(phoneNumber)) {
            input_layout_phone_number.setError(getString(R.string.error_phone_number_length));
            return false;
        } else input_layout_phone_number.setError(null);

        if (houseNo.isEmpty()) {
            input_layout_house_no.setError(getString(R.string.error_required));
            return false;
        } else input_layout_house_no.setError(null);

        if (street.isEmpty()) {
            input_layout_street.setError(getString(R.string.error_required));
            return false;
        } else input_layout_street.setError(null);

        if (city.isEmpty()) {
            input_layout_city.setError(getString(R.string.error_required));
            return false;
        } else input_layout_city.setError(null);

        if (landmark.isEmpty()) {
            input_layout_landmark.setError(getString(R.string.error_required));
            return false;
        } else input_layout_landmark.setError(null);

        if (state.isEmpty()) {
            input_layout_state.setError(getString(R.string.error_required));
            return false;
        } else input_layout_state.setError(null);

        if (pincode.isEmpty()) {
            input_layout_pincode.setError(getString(R.string.error_required));
            return false;
        } else if (!ValidationUtils.isValidPincode(pincode)) {
            input_layout_pincode.setError(getString(R.string.error_pincode_length));
            return false;
        } else input_layout_pincode.setError(null);


        String address = houseNo + ", " + street + ", " + city + ", " + landmark + ", " + state + "," + pincode;
        shippingAddressItem = new ShippingAddressItem(name, address, "+91" + phoneNumber);

        return true;
    }

}
