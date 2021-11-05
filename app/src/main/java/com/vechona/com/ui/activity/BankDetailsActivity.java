package com.vechona.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.BankDetailsResponse;
import com.vechona.com.data.remote.apiResponse.UpdateBankDetailsResponse;
import com.vechona.com.ui.model.BankAccountDetails;
import com.vechona.com.ui.model.User;
import com.vechona.com.ui.utils.ViewUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankDetailsActivity extends AppCompatActivity {

    @BindView(R.id.bank_details)
    CardView bank_details;

    @BindView(R.id.input_layout_account)
    TextInputLayout input_layout_account;

    @BindView(R.id.input_layout_confirm_account)
    TextInputLayout input_layout_confirm_account;

    @BindView(R.id.input_layout_name)
    TextInputLayout input_layout_name;

    @BindView(R.id.input_layout_ifsc)
    TextInputLayout input_layout_ifsc;

    @BindView(R.id.etAccount)
    EditText etAccount;

    @BindView(R.id.etConfirmAccount)
    EditText etConfirmAccount;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etIfsc)
    EditText etIfsc;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private String confirmAccount, account, holdersName, ifsc;
    private BankAccountDetails bankAccountDetails;
    private User user;
    private PreferenceDataHelper preferenceDataHelper;
    private ApiService apiService;

    public static void start(Context context) {
        Intent starter = new Intent(context, BankDetailsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
        ButterKnife.bind(this);
        initView();
        initData();
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
    }

    private void initData() {
        apiService =
                RemoteServiceHelper.createService(ApiService.class,
                        RemoteServiceHelper.getRetrofitInstance(BankDetailsActivity.this));

        preferenceDataHelper = PreferenceDataHelper.getInstance(BankDetailsActivity.this);

        bankAccountDetails = preferenceDataHelper.getUser().getAccountDetails();
        user = preferenceDataHelper.getUser();
        if (bankAccountDetails != null) {
            etAccount.setText(bankAccountDetails.getAccountNo());
            etName.setText(bankAccountDetails.getAccountHolderName());
            etIfsc.setText(bankAccountDetails.getIfscCode());
            etConfirmAccount.setText(bankAccountDetails.getAccountNo());
            btn_submit.setText(R.string.update);
        } else
            btn_submit.setText(R.string.submit);
    }

    @OnClick(R.id.btn_submit)
    public void onClickSubmit() {
        if (validation()) {
            BankAccountDetails accountDetails = new BankAccountDetails(account,
                    holdersName,
                    ifsc,
                    user.getUserID());
            if (bankAccountDetails != null) {
                updateBankDetails(accountDetails);
            } else {
                storeBankDetails(accountDetails);
            }
        }
    }

    private void updateBankDetails(BankAccountDetails accountDetails) {
        progressWheel.setVisibility(View.VISIBLE);
        user.setAccountDetails(accountDetails);
        apiService.updateBankDetail(accountDetails).enqueue(new Callback<UpdateBankDetailsResponse>() {
            @Override
            public void onResponse(Call<UpdateBankDetailsResponse> call, Response<UpdateBankDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("success")) {
                    progressWheel.setVisibility(View.GONE);
                    preferenceDataHelper.saveUser(user);
                    ViewUtils.showToast(BankDetailsActivity.this, getString(R.string.msg_bank_details_saved_successfully));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UpdateBankDetailsResponse> call, Throwable t) {
                progressWheel.setVisibility(View.GONE);
                ViewUtils.showToast(BankDetailsActivity.this, getString(R.string.error_bank_details_save_failed));
            }
        });
    }

    private void storeBankDetails(BankAccountDetails accountDetails) {
        user.setAccountDetails(accountDetails);
        apiService.bankUser(accountDetails).enqueue(new Callback<BankDetailsResponse>() {
            @Override
            public void onResponse(Call<BankDetailsResponse> call, Response<BankDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equals("success")) {
                    preferenceDataHelper.saveUser(user);
                    ViewUtils.showToast(BankDetailsActivity.this, getString(R.string.msg_bank_details_saved_successfully));
                    finish();
                } else
                    ViewUtils.showToast(BankDetailsActivity.this, getString(R.string.error_bank_details_save_failed));
            }

            @Override
            public void onFailure(Call<BankDetailsResponse> call, Throwable t) {
                ViewUtils.showToast(BankDetailsActivity.this, getString(R.string.error_bank_details_save_failed));
            }
        });
    }

    private boolean validation() {
        confirmAccount = etConfirmAccount.getText().toString();
        account = etAccount.getText().toString();
        holdersName = etName.getText().toString();
        ifsc = etIfsc.getText().toString();

        if (isEmpty(account) || isEmpty(confirmAccount) || isEmpty(holdersName) || isEmpty(ifsc)) {
            input_layout_account.setError(getString(R.string.error_required));
            input_layout_confirm_account.setError(getString(R.string.error_required));
            input_layout_name.setError(getString(R.string.error_required));
            input_layout_ifsc.setError(getString(R.string.error_required));
            return false;
        } else if (account.contains(" ") || !(account.length() > 11 && account.length() <= 16)) {
            if (!(account.length() > 11 && account.length() <= 16)) {
                input_layout_account.setError(getString(R.string.error_account_no_length));
                return false;
            } else {
                input_layout_account.setError(getString(R.string.error_account_no_space));
                return false;
            }
        } else if (!(account.equals(confirmAccount))) {
            input_layout_confirm_account.setError(getString(R.string.error_confirm_account_no_match));
            return false;
        } else if (isEmpty(holdersName)) {
            input_layout_name.setError(getString(R.string.error_required));
            return false;

        } else if (!validateIFSCCode(ifsc)) {
            input_layout_ifsc.setError(getString(R.string.error_invalid_ifsc_code));
            return false;
        }
        return true;
    }

    boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    boolean validateIFSCCode(String s) {
        String regx = "^[A-Za-z]{4}[0-9]{7}$";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }
}
