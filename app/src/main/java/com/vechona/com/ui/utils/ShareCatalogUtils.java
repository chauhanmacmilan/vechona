package com.vechona.com.ui.utils;

import android.content.Context;

import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.apiResponse.WishOrSharedListResponse;
import com.vechona.com.ui.model.Catalog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShareCatalogUtils {

    public static void deleteFromWishListAPI(ApiService apiService, final Context context, final Catalog catalog) {
        apiService.deleteWishList(PreferenceDataHelper.getInstance(context).getUser().getUserID(), catalog.getCatalogId())
                .enqueue(new Callback<WishOrSharedListResponse>() {
                    @Override
                    public void onResponse(Call<WishOrSharedListResponse> call, Response<WishOrSharedListResponse> response) {
                        WishOrSharedListResponse.Data data = response.body().getData();
                        if (response.isSuccessful() && data != null) {
                            PreferenceDataHelper.getInstance(context).removeFaverite(catalog, true);
                            ViewUtils.showToast(context, data.getResult());
                        }
                    }

                    @Override
                    public void onFailure(Call<WishOrSharedListResponse> call, Throwable t) {
                        ViewUtils.showToast(context, context.getString(R.string.error_connection_failed));
                    }
                });
    }

    public static void addWishOrSharedCatalog(ApiService apiService, final Context context, final Catalog catalog, boolean isWishList) {
        if (isWishList) {
            apiService.addWishList(new Catalog(PreferenceDataHelper.getInstance(context).getUser().getUserID(), catalog.getCatalogId()))
                    .enqueue(new Callback<WishOrSharedListResponse>() {
                        @Override
                        public void onResponse(Call<WishOrSharedListResponse> call, Response<WishOrSharedListResponse> response) {
                            if (response.isSuccessful() && response.body().getData() != null) {
                                PreferenceDataHelper.getInstance(context).addFavorite(catalog, true);
                                ViewUtils.showToast(context, response.body().getData().getResult());
                            }
                        }

                        @Override
                        public void onFailure(Call<WishOrSharedListResponse> call, Throwable t) {
                            ViewUtils.showToast(context, context.getString(R.string.error_connection_failed));
                        }
                    });
        } else {
            apiService.addSharedItem(new Catalog(PreferenceDataHelper.getInstance(context).getUser().getUserID(), catalog.getCatalogId()))
                    .enqueue(new Callback<WishOrSharedListResponse>() {
                        @Override
                        public void onResponse(Call<WishOrSharedListResponse> call, Response<WishOrSharedListResponse> response) {
                            if (response.isSuccessful() && response.body().getData() != null) {
                                PreferenceDataHelper.getInstance(context).addFavorite(catalog, false);
                                ViewUtils.showToast(context, response.body().getData().getResult());
                            }
                        }

                        @Override
                        public void onFailure(Call<WishOrSharedListResponse> call, Throwable t) {
                            ViewUtils.showToast(context, context.getString(R.string.error_connection_failed));
                        }
                    });
        }
    }
}
