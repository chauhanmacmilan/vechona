package com.vechona.com.ui.onboarding.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.AuthResponse;
import com.vechona.com.data.remote.apiResponse.AuthResponseData;
import com.vechona.com.data.remote.apiResponse.CollectionItemResponse;
import com.vechona.com.data.remote.apiResponse.CollectionItemResponseData;
import com.vechona.com.service.MyFirebaseMessagingService;
import com.vechona.com.ui.activity.HomeActivity;
import com.vechona.com.ui.model.Catalog;
import com.vechona.com.ui.model.UserRequest;
import com.vechona.com.ui.utils.ViewUtils;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class LandingFragment extends Fragment {

    private static final int RC_SIGN_IN = 1;
    private ApiService apiService;
    private List<Catalog> faverite;
    private List<Catalog> sharedList;
    private PreferenceDataHelper preferenceDataHelper;

    @BindView(R.id.progress_wheel)
    ProgressWheel progress_wheel;

    @BindView(R.id.button_start)
    Button btnStart;

    public static LandingFragment newInstance() {
        Bundle args = new Bundle();
        LandingFragment fragment = new LandingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        apiService =
                RemoteServiceHelper.createService(ApiService.class,
                        RemoteServiceHelper.getRetrofitInstance(getContext()));
        preferenceDataHelper = PreferenceDataHelper.getInstance(getContext());
    }

    @OnClick(R.id.button_start)
    public void onClickStart() {
        btnStart.setVisibility(View.GONE);
        progress_wheel.setVisibility(View.VISIBLE);
        List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.PhoneBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            //IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    authUser(user);
                }
            } else {
                progress_wheel.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "connection failed", Toast.LENGTH_SHORT).show();
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private void authUser(FirebaseUser user) {
        apiService
                .authUser(new UserRequest(user.getPhoneNumber(), user.getUid()))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                        AuthResponseData authResponseData = response.body().getAuthResponseData();
                        if (response.isSuccessful() && authResponseData != null) {
                            authResponseData.getUser().setAccountDetails(authResponseData.getAccountDetails());
                            preferenceDataHelper.saveUser(authResponseData.getUser());
                            preferenceDataHelper.saveAccessToken(authResponseData.getAccessToken());
                            getFaveriteFromServer(authResponseData.getUser().getUserID());
                            getSharedItemFromAPI(authResponseData.getUser().getUserID());
                            retrieveCurrentRegistrationToken();
                            progress_wheel.setVisibility(View.GONE);
                            HomeActivity.start(getContext());
                        } else {
                            Toast.makeText(getContext(), "response fail", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), "failer ", Toast.LENGTH_LONG).show();
                        progress_wheel.setVisibility(View.GONE);
                        btnStart.setVisibility(View.VISIBLE);
                        Log.d("onFailure: ", t.getMessage());
                    }
                });
    }

    private void retrieveCurrentRegistrationToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        preferenceDataHelper.saveFCMToken(token);
                        MyFirebaseMessagingService.sendRegistrationToServer(token, getContext());
                    }
                });
    }

    private void getSharedItemFromAPI(int userID) {
        apiService.getSharedList(userID).enqueue(new Callback<CollectionItemResponse>() {
            @Override
            public void onResponse(@NonNull Call<CollectionItemResponse> call, @NonNull Response<CollectionItemResponse> response) {
                CollectionItemResponseData collectionItemResponseData = response.body().getCollectionItemResponseData();
                if (response.isSuccessful() && collectionItemResponseData != null) {
                    sharedList = collectionItemResponseData.getCataloglist();
                    PreferenceDataHelper.getInstance(getContext()).saveFavorites(sharedList, false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CollectionItemResponse> call, @NonNull Throwable t) {
                ViewUtils.showToast(getContext(), getString(R.string.error_connection_failed));
            }
        });
    }

    private void getFaveriteFromServer(int userId) {
        apiService.getWishList(userId).enqueue(new Callback<CollectionItemResponse>() {
            @Override
            public void onResponse(@NonNull Call<CollectionItemResponse> call, @NonNull Response<CollectionItemResponse> response) {
                CollectionItemResponseData collectionItemResponseData = response.body().getCollectionItemResponseData();
                if (response.isSuccessful() && collectionItemResponseData != null) {
                    faverite = collectionItemResponseData.getCataloglist();
                    PreferenceDataHelper.getInstance(getContext()).saveFavorites(faverite, true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CollectionItemResponse> call, @NonNull Throwable t) {
                ViewUtils.showToast(getContext(), getString(R.string.error_connection_failed));
            }
        });
    }
}
