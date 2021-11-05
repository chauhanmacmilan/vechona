package com.vechona.com.ui.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vechona.app.BuildConfig;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.CollectionItemResponse;
import com.vechona.com.data.remote.apiResponse.CollectionItemResponseData;
import com.vechona.com.data.remote.apiResponse.HomeSliderResponse;
import com.vechona.com.data.remote.apiResponse.HomeSliderResponseData;
import com.vechona.com.data.remote.apiResponse.TopCategoryResponse;
import com.vechona.com.data.remote.apiResponse.TopCategoryResponseData;
import com.vechona.com.ui.activity.CartActivity;
import com.vechona.com.ui.activity.CatalogActivity;
import com.vechona.com.ui.activity.CatalogListActivity;
import com.vechona.com.ui.activity.ShareCatalogsActivity;
import com.vechona.com.ui.adapter.CatalogListAdapter;
import com.vechona.com.ui.adapter.CategoriesAdapter;
import com.vechona.com.ui.custom.CustomAdapter;
import com.vechona.com.ui.interfaces.ItemClickListener;
import com.vechona.com.ui.interfaces.OnItemClickListener;
import com.vechona.com.ui.model.Catalog;
import com.vechona.com.ui.model.Category;
import com.vechona.com.ui.model.HomeSlider;
import com.vechona.com.ui.utils.ImageDownloadUtils;
import com.vechona.com.ui.utils.ShareCatalogUtils;
import com.vechona.com.ui.utils.ViewUtils;
import com.vechona.com.ui.utils.WhatsAppShareUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private static final int PERMISSION_REQUEST_WRITE_DATA = 2;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.indicator)
    CircleIndicator indicator;

    @BindView(R.id.rvCategoties)
    RecyclerView rvCategories;

    @BindView(R.id.rvHomeCatalog)
    RecyclerView rvHomeCatalog;

    @BindView(R.id.tvCartBadge)
    TextView tvCartBadge;

    @BindView(R.id.placeholder)
    View placeholder;

    @BindView(R.id.llPlaceholder2)
    LinearLayout llPlaceholder2;

    @BindView(R.id.llPlaceholder3)
    LinearLayout llPlaceholder3;

    private int currentPage = 0;
    private CategoriesAdapter categoriesAdapter;
    private PagerAdapter sliderAdapter;
    private ApiService apiService;
    private List<Category> topCategories;
    private CatalogListAdapter catalogAdapter;
    private List<Catalog> catalogs;
    private boolean isWhatsappShare;
    private int catalogId1;
    private PreferenceDataHelper preferenceDataHelper;
    private int margin1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @OnClick(R.id.ivCart)
    public void openCartActivity() {
        CartActivity.start(getContext());
    }

    @OnClick(R.id.ivWishlist)
    public void onClickWishList() {
        ShareCatalogsActivity.start(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = RemoteServiceHelper.createService(ApiService.class,
                        RemoteServiceHelper.getRetrofitInstance(getContext()));
        preferenceDataHelper = PreferenceDataHelper.getInstance(getContext());

        /*sliderAdapter = new CustomAdapter();
        viewPager.setAdapter(sliderAdapter);

        ViewCompat.setNestedScrollingEnabled(viewPager, false);
        ViewCompat.setNestedScrollingEnabled(rvCategories, false);

        indicator.setViewPager(viewPager);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 3000);*/

        initView();
    }

    private void initView() {
        categoriesAdapter = new CategoriesAdapter(new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CatalogListActivity.start(getContext(), topCategories.get(position).getCategoryName());
            }
        });
        rvCategories.setAdapter(categoriesAdapter);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        catalogAdapter = new CatalogListAdapter(new OnItemClickListener() {
            @Override
            public void onClickAtPosition(View view, int position) {
                CatalogActivity.start(getContext(), getCatalogById(position)/*catalogs.get(position)*/);
            }

            @Override
            public void onClickWishList(View view, int position) {
                Catalog catalog = getCatalogById(position);
                if (!preferenceDataHelper.isWishOrShareList(catalog, true)) {
                    ShareCatalogUtils.addWishOrSharedCatalog(apiService, getContext(), catalog, true);
                } else {
                    ShareCatalogUtils.deleteFromWishListAPI(apiService, getContext(), catalog);
                }
            }

            @Override
            public void onClickShareNow(View view, int position) {
                catalogId1 = position;
                showSetMarginDialog();
            }
        });
        rvHomeCatalog.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvHomeCatalog.setAdapter(catalogAdapter);
    }

    private void showSetMarginDialog() {
        String tag = "SetMarginDialog";
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            return;
        }
        SetMarginDialog dialog = SetMarginDialog.newInstance();
        dialog.setMarginListener(new SetMarginDialog.SetMarginDialogListener() {
            @Override
            public void onSetMarginDialogSubmitClick(int margin) {
                margin1 = margin;
                shareWhatsappImage();
            }
        });
        dialog.show(fragmentTransaction, tag);
    }

    private void shareWhatsappImage() {
        if (WhatsAppShareUtils.isWhatsAppInstalled(getContext())) {
            Catalog catalog = getCatalogById(catalogId1);
            isWhatsappShare = true;
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                checkRuntimePermission(PERMISSION_REQUEST_WRITE_DATA);
            } else {
                ImageDownloadUtils.shareImages(getContext(), catalog, null);
                if (!preferenceDataHelper.isWishOrShareList(catalog, false)) {
                    ShareCatalogUtils.addWishOrSharedCatalog(apiService, getContext(), catalog, false);
                }
            }
        } else
            ViewUtils.showToast(getContext(), getString(R.string.whatsapp_not_installed));
    }

    private void checkRuntimePermission(final int request_id) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.dialog_title)
                    .setMessage(R.string.denied_permission)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, request_id);
                        }
                    }).show();

        } else {
            if (preferenceDataHelper.isFirstTimeAskingPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                preferenceDataHelper.firstTimeAskingPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, false);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, request_id);
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.dialog_title)
                        .setMessage(R.string.disable_permission)
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                            }
                        }).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_WRITE_DATA: {
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Catalog catalog = getCatalogById(catalogId1);
                    ImageDownloadUtils.shareImages(getContext(), catalog, null);
                    if (!preferenceDataHelper.isWishOrShareList(catalog, false)) {
                        ShareCatalogUtils.addWishOrSharedCatalog(apiService, getContext(), catalog, false);
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        shareWhatsappText();
        setCartBadgeCount();
    }

    private void shareWhatsappText() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isWhatsappShare) {
                    isWhatsappShare = false;
                    int price = getCatalogById(catalogId1).getCatalogBasePrice() + margin1;
                    String description = getString(R.string.title_cart_price) + " : " + price +
                            "\n" + getCatalogById(catalogId1).getCatalogDescription();
                    WhatsAppShareUtils.shareText(description, getContext());
                }
            }
        }, 600);
        catalogAdapter.notifyDataSetChanged();
    }

    private Catalog getCatalogById(int catalogId) {
        Catalog catalog1 = null;
        for (Catalog catalog : catalogs) {
            if (catalog.getCatalogId() == catalogId) {
                catalog1 = catalog;
                break;
            }
        }
        return catalog1;
    }

    private void setCartBadgeCount() {
        int count = preferenceDataHelper.getCartItemCount();
        if (count == 0) tvCartBadge.setVisibility(View.GONE);
        else {
            tvCartBadge.setVisibility(View.VISIBLE);
            tvCartBadge.setText(String.valueOf(count));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getTopCategory();
        getSliderData();
        getCatalogData();
    }

    private void getSliderData() {
        placeholder.setVisibility(View.VISIBLE);
        apiService.getHomeSliderData().enqueue(new Callback<HomeSliderResponse>() {
            @Override
            public void onResponse(@NonNull Call<HomeSliderResponse> call, @NonNull Response<HomeSliderResponse> response) {
                HomeSliderResponseData homeSliderResponseData = response.body().getHomeSliderResponseData();
                if (response.isSuccessful() && homeSliderResponseData != null) {
                    viewPager.setVisibility(View.VISIBLE);
                    placeholder.setVisibility(View.GONE);
                    final List<HomeSlider> sliderList = homeSliderResponseData.getSliderList();
                    sliderAdapter = new CustomAdapter(sliderList);
                    viewPager.setAdapter(sliderAdapter);

                    ViewCompat.setNestedScrollingEnabled(viewPager, false);
                    indicator.setViewPager(viewPager);

                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == sliderList.size()) {
                                currentPage = 0;
                            }
                            viewPager.setCurrentItem(currentPage++, true);
                        }
                    };

                    Timer timer = new Timer(); // This will create a new Thread
                    timer.schedule(new TimerTask() { // task to be scheduled
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 500, 3000);
                    //sliderAdapter.setItems(sliderList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomeSliderResponse> call, @NonNull Throwable t) {
                viewPager.setVisibility(View.VISIBLE);
                placeholder.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("MilanCheking", t.getMessage());
                Log.d("MilanCheking", preferenceDataHelper.getAccessToken());
                Log.d("MilanCheking", preferenceDataHelper.getFcmToken());
            }
        });
    }


    private void getTopCategory() {
        apiService.getTopCategoryResponse().enqueue(new Callback<TopCategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<TopCategoryResponse> call, @NonNull Response<TopCategoryResponse> response) {
                TopCategoryResponseData topCategoryResponseData = response.body().getTopCategoryResponseData();
                if (response.isSuccessful() && topCategoryResponseData != null) {
                    llPlaceholder2.setVisibility(View.GONE);
                    topCategories = topCategoryResponseData.getCategoryList();
                    categoriesAdapter.setItems(topCategories);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TopCategoryResponse> call, @NonNull Throwable t) {
                llPlaceholder2.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("MilanCheking", t.getMessage());
                Log.d("MilanCheking", preferenceDataHelper.getAccessToken());
                Log.d("MilanCheking", preferenceDataHelper.getFcmToken());
            }
        });
    }

    private void getCatalogData() {
        apiService.getHomeCatalog().enqueue(new Callback<CollectionItemResponse>() {
            @Override
            public void onResponse(@NonNull Call<CollectionItemResponse> call, @NonNull Response<CollectionItemResponse> response) {
                CollectionItemResponseData collectionItemResponseData = response.body().getCollectionItemResponseData();
                if (response.isSuccessful() && collectionItemResponseData != null) {
                    llPlaceholder3.setVisibility(View.GONE);
                    catalogs = collectionItemResponseData.getCataloglist();
                    catalogAdapter.setCatalogList(catalogs);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CollectionItemResponse> call, @NonNull Throwable t) {
                llPlaceholder3.setVisibility(View.GONE);
                ViewUtils.showToast(getContext(), t.getMessage());
                Log.d("MilanCheking", t.getMessage());
                Log.d("MilanCheking", preferenceDataHelper.getAccessToken());
                Log.d("MilanCheking", preferenceDataHelper.getFcmToken());
            }
        });
    }

    @OnClick(R.id.llsearch)
    public void onClickSearch() {
        CatalogListActivity.start(getContext(), true);
    }
}
