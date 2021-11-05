package com.vechona.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.UserUpdateResponse;
import com.vechona.com.ui.model.User;
import com.vechona.com.ui.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.etFullName)
    EditText etFullName;
    @BindView(R.id.etMobileNo)
    EditText etMobileNo;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPincode)
    EditText etPincode;
    @BindView(R.id.etAddress1)
    EditText etAddress1;
    @BindView(R.id.etAddress2)
    EditText etAddress2;
    @BindView(R.id.etCity)
    EditText etCity;
    @BindView(R.id.etState)
    EditText etState;
    @BindView(R.id.etCountry)
    EditText etCountry;

    @BindView(R.id.input_layout_fullname)
    TextInputLayout input_layout_fullname;
    @BindView(R.id.input_layout_email)
    TextInputLayout input_layout_email;
    @BindView(R.id.input_layout_pincode)
    TextInputLayout input_layout_pincode;
    @BindView(R.id.input_layout_address1)
    TextInputLayout input_layout_address1;
    @BindView(R.id.input_layout_address2)
    TextInputLayout input_layout_address2;
    @BindView(R.id.input_layout_city)
    TextInputLayout input_layout_city;
    @BindView(R.id.input_layout_state)
    TextInputLayout input_layout_state;
    @BindView(R.id.input_layout_country)
    TextInputLayout input_layout_country;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private User user;
    private PreferenceDataHelper preferenceDataHelper;

    public static void start(Context context) {
        Intent intent = new Intent(context, EditProfileActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        preferenceDataHelper = PreferenceDataHelper.getInstance(EditProfileActivity.this);
        user = preferenceDataHelper.getUser();
        setUpCurrentUserData();
    }

    private void setUpCurrentUserData() {
        etFullName.setText((user.getFirstName() != null && user.getLastName() != null) ? user.getFirstName() + " " + user.getLastName() : "");
        etMobileNo.setText(user.getPhoneNumber());
        etEmail.setText(user.getEmail());
        etPincode.setText(user.getPincode());
        etAddress1.setText(user.getAddress1());
        etAddress2.setText(user.getAddress2());
        etCity.setText(user.getCity());
        etState.setText(user.getState());
        etCountry.setText(user.getCountry());

    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etCountry.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    findViewById(R.id.menu_save).performClick();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                if (validate()) {
                    updateUser();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean validate() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pinCode = etPincode.getText().toString().trim();
        String address1 = etAddress1.getText().toString().trim();
        String address2 = etAddress2.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String state = etState.getText().toString().trim();
        String country = etCountry.getText().toString().trim();

        if (fullName.isEmpty()) {
            input_layout_fullname.setError(getString(R.string.error_required));
            return false;
        } else input_layout_fullname.setError(null);

        if (email.isEmpty()) {
            input_layout_email.setError(getString(R.string.error_required));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            input_layout_email.setError(getString(R.string.error_invalid_email));
            return false;
        } else input_layout_email.setError(null);

        if (pinCode.isEmpty()) {
            input_layout_pincode.setError(getString(R.string.error_required));
            return false;
        } else if (pinCode.length() != 6) {
            input_layout_pincode.setError(getString(R.string.error_pincode_length));
            return false;
        } else input_layout_pincode.setError(null);


        if (address1.isEmpty()) {
            input_layout_address1.setError(getString(R.string.error_required));
            return false;
        } else input_layout_address1.setError(null);

        if (address2.isEmpty()) {
            input_layout_address2.setError(getString(R.string.error_required));
            return false;
        } else input_layout_address2.setError(null);


        if (city.isEmpty()) {
            input_layout_city.setError(getString(R.string.error_required));
            return false;
        } else input_layout_city.setError(null);


        if (state.isEmpty()) {
            input_layout_state.setError(getString(R.string.error_required));
            return false;
        } else input_layout_state.setError(null);

        if (country.isEmpty()) {
            input_layout_country.setError(getString(R.string.error_required));
            return false;
        } else input_layout_country.setError(null);

        if (fullName.split(" ").length == 1) {
            user.setFirstName(fullName.split(" ")[0]);
            user.setLastName("");
        } else {
            user.setFirstName(fullName.split(" ")[0]);
            user.setLastName(fullName.split(" ")[1]);
        }
        user.setEmail(email);
        user.setPincode(pinCode);
        user.setAddress1(address1);
        user.setAddress2(address2);
        user.setCity(city);
        user.setState(state);
        user.setCountry(country);
        return true;
    }

    private void updateUser() {
        ApiService apiService =
                RemoteServiceHelper.createService(ApiService.class,
                        RemoteServiceHelper.getRetrofitInstance(EditProfileActivity.this));
        progressWheel.setVisibility(View.VISIBLE);

        apiService
                .updateUser(user)
                .enqueue(new Callback<UserUpdateResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UserUpdateResponse> call, @NonNull Response<UserUpdateResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            progressWheel.setVisibility(View.GONE);
                            preferenceDataHelper.saveUser(user);
                            finish();
                            ViewUtils.showToast(EditProfileActivity.this, getString(R.string.msg_profile_update_successfully));
                        } else {
                            ViewUtils.showToast(EditProfileActivity.this, getString(R.string.error_profile_update_failed));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserUpdateResponse> call, @NonNull Throwable t) {
                        progressWheel.setVisibility(View.GONE);
                        ViewUtils.showToast(EditProfileActivity.this, getString(R.string.error_profile_update_failed));
                    }
                });
    }


}
