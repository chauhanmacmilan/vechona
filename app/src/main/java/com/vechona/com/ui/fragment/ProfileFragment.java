package com.vechona.com.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.UserResponse;
import com.vechona.com.ui.activity.BankDetailsActivity;
import com.vechona.com.ui.activity.CartActivity;
import com.vechona.com.ui.activity.CatalogListActivity;
import com.vechona.com.ui.activity.EditProfileActivity;
import com.vechona.com.ui.activity.MyPaymentsActivity;
import com.vechona.com.ui.activity.ShareCatalogsActivity;
import com.vechona.com.ui.onboarding.views.OnboardingActivity;
import com.vechona.com.ui.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    @BindView(R.id.llProfile)
    LinearLayout llProfile;

    @BindView(R.id.titleTV)
    TextView toolbar_title;

    @BindView(R.id.ivMenuSearch)
    AppCompatImageView ivMenuSearch;

    @BindView(R.id.rlCart)
    RelativeLayout rlCart;

    @BindView(R.id.tvCartBadge)
    TextView tvCartBadge;

    @BindView(R.id.tvPhoneNo)
    TextView tvPhoneNo;

    @BindView(R.id.ivMenu)
    AppCompatImageView ivMenu;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        ivMenu.setVisibility(View.VISIBLE);
        ivMenuSearch.setVisibility(View.VISIBLE);
        rlCart.setVisibility(View.VISIBLE);
        toolbar_title.setText(R.string.menu_account);

        initData();
        return view;
    }

    private void initData() {
        tvPhoneNo.setText(PreferenceDataHelper.getInstance(getContext()).getUser().getPhoneNumber());
    }


    @OnClick(R.id.llbankdetail)
    public void onClickBankDetails() {
        BankDetailsActivity.start(getContext());
    }

    @OnClick(R.id.ivMenu)
    public void onclickMenu() {
        PopupMenu popup = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            popup = new PopupMenu(getContext(), getView().findViewById(R.id.ivMenu), Gravity.NO_GRAVITY, R.attr.actionOverflowMenuStyle, 0);
        }
        popup.getMenuInflater().inflate(R.menu.main_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout: {
                        userLogout();
                        return true;
                    }

                    default: {
                        return false;
                    }
                }
            }
        });
        popup.show();
    }

    @OnClick(R.id.rlCart)
    public void onClickCart() {
        CartActivity.start(getContext());
    }

    @OnClick(R.id.ivMenuSearch)
    public void onClickSearch() {
        CatalogListActivity.start(getContext(), true);
    }

    private void userLogout() {
        ApiService apiService = RemoteServiceHelper
                .createService(ApiService.class,
                        RemoteServiceHelper.getRetrofitInstance(getContext()));
        apiService.logoutUser(PreferenceDataHelper.getInstance(getContext()).getUser())
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        UserResponse.Data data = response.body().getData();
                        if (response.isSuccessful() && data != null) {
                            AuthUI.getInstance()
                                    .signOut(getContext())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        public void onComplete(@NonNull Task<Void> task) {
                                            PreferenceDataHelper.getInstance(getContext()).clearUser();
                                            OnboardingActivity.start(getContext());
                                        }
                                    });
                            ViewUtils.showToast(getContext(), data.getResult());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        ViewUtils.showToast(getContext(), getString(R.string.error_connection_failed));
                    }
                });
    }

    @OnClick(R.id.tvEditProfile)
    public void onClickEditProfile() {
        EditProfileActivity.start(getContext());
    }

    @OnClick(R.id.llPayment)
    public void onClickPayment() {
        MyPaymentsActivity.start(getContext());
    }

    @OnClick(R.id.llSharedCatalogs)
    public void onClickShareCatalog() {
        ShareCatalogsActivity.start(getContext());
    }

    @OnClick(R.id.llRate)
    public void onClickllRate() {
        Uri page = Uri.parse("market://details?id=" + "com.squareapp.expensetracker");
        Intent intent = new Intent(Intent.ACTION_VIEW, page);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            String uri = "https://play.google.com/store/apps/details?id=" + "com.squareapp.expensetracker";
            intent = new Intent(Intent.ACTION_VIEW, Uri
                    .parse(uri));
            startActivity(Intent.createChooser(intent,
                    "Rate App!!"));
        }
    }

    @OnClick(R.id.llEmailus)
    public void onClickEmailus() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@squareinfosoft.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "hello. this is a message sent to ecom app");
        intent.setType("text/html");
        intent.setPackage("com.google.android.gm");
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        setCartBadgeCount();
    }

    private void setCartBadgeCount() {
        int count = PreferenceDataHelper.getInstance(getContext()).getCartItemCount();
        if (count == 0) tvCartBadge.setVisibility(View.GONE);
        else {
            tvCartBadge.setVisibility(View.VISIBLE);
            tvCartBadge.setText(String.valueOf(count));
        }
    }
}
