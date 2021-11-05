package com.vechona.com.ui.activity;

import android.Manifest;
import android.content.Context;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.vechona.app.BuildConfig;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.CollectionItemResponse;
import com.vechona.com.data.remote.apiResponse.CollectionItemResponseData;
import com.vechona.com.data.remote.apiResponse.TopCategory;
import com.vechona.com.data.remote.apiResponse.TopCategoryData;
import com.vechona.com.ui.adapter.CatalogListAdapter;
import com.vechona.com.ui.fragment.FilterBottomSheet;
import com.vechona.com.ui.fragment.SetMarginDialog;
import com.vechona.com.ui.fragment.SortBottomSheet;
import com.vechona.com.ui.interfaces.OnItemClickListener;
import com.vechona.com.ui.model.Catalog;
import com.vechona.com.ui.model.Filter;
import com.vechona.com.ui.model.FilterItem;
import com.vechona.com.ui.utils.BundleParams;
import com.vechona.com.ui.utils.ImageDownloadUtils;
import com.vechona.com.ui.utils.ShareCatalogUtils;
import com.vechona.com.ui.utils.StringUtils;
import com.vechona.com.ui.utils.ViewUtils;
import com.vechona.com.ui.utils.WhatsAppShareUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogListActivity extends AppCompatActivity implements SortBottomSheet.OnCheckChanged, FilterBottomSheet.OnApplyChanged {

    private static final int PERMISSION_REQUEST_WRITE_DATA = 2;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rlCart)
    RelativeLayout rlCart;

    @BindView(R.id.tvCartBadge)
    TextView tvCartBadge;

    @BindView(R.id.titleTV)
    TextView title_toolbar;

    @BindView(R.id.rvCataloglist)
    RecyclerView rvCataloglist;

    @BindView(R.id.tvEmptyCatalog)
    TextView tvEmptyCatalog;

    @BindView(R.id.llsort)
    LinearLayout llsort;

    @BindView(R.id.llfilter)
    LinearLayout llfilter;

    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    @BindView(R.id.tvSortType)
    TextView tvSortType;

    private CatalogListAdapter catalogListAdapter;
    private boolean isWhatsappShare;
    private int catalogId1;
    private List<Catalog> catalogs;
    private int collectionId;
    private PreferenceDataHelper pref;
    private String categoryName;
    private String is_from;
    private ApiService apiService;
    private boolean is_searchable;
    private String checkedText = null;
    private int margin1;

    private String[] strRating = {"1.0 and above", "2.0 and above", "3.0 and above", "4.0 and above", "New Catalogs"};
    private String[] strPrice = {"Rs.0 - Rs.250", "Rs.250 - Rs.500", "Rs.500 - Rs.750", "Rs.750 & Above"};

    private List<FilterItem> categoryFilterList = new ArrayList<>();
    private List<FilterItem> priceFilterList = new ArrayList<>();
    private List<FilterItem> ratingFilterList = new ArrayList<>();
    private List<FilterItem> discountFilterList = new ArrayList<>();
    private List<Filter> filterList = new ArrayList<>();

    public static void start(Context context, int collectionId, String collectionName) {
        Intent intent = new Intent(context, CatalogListActivity.class);
        intent.putExtra(BundleParams.COLLECTION_ID, collectionId);
        intent.putExtra(BundleParams.COLLECTION_NAME, collectionName);
        intent.putExtra(BundleParams.IS_FROM, "Collection");
        context.startActivity(intent);
    }

    public static void start(Context context, String categoryName) {
        Intent intent = new Intent(context, CatalogListActivity.class);
        intent.putExtra(BundleParams.CATEGORY_NAME, categoryName);
        intent.putExtra(BundleParams.IS_FROM, "Top_Catagory");
        context.startActivity(intent);
    }

    public static void start(Context context, boolean isSearchable) {
        Intent intent = new Intent(context, CatalogListActivity.class);
        intent.putExtra(BundleParams.IS_SEARCHABLE, isSearchable);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cataloglist);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        apiService = RemoteServiceHelper.createService(ApiService.class,
                RemoteServiceHelper.getRetrofitInstance(this));
        pref = PreferenceDataHelper.getInstance(this);

        llsort.setEnabled(false);
        llfilter.setEnabled(false);

        is_searchable = getIntent().getExtras().getBoolean(BundleParams.IS_SEARCHABLE);

        if (!is_searchable) {
            is_from = getIntent().getExtras().getString(BundleParams.IS_FROM);
            if (is_from.equals("Top_Catagory")) {
                categoryName = getIntent().getExtras().getString(BundleParams.CATEGORY_NAME);
                getSupportActionBar().setTitle(StringUtils.camelCase(categoryName));
            } else {
                collectionId = getIntent().getExtras().getInt(BundleParams.COLLECTION_ID);
                String collectionName = getIntent().getExtras().getString(BundleParams.COLLECTION_NAME);
                getSupportActionBar().setTitle(StringUtils.camelCase(collectionName));
            }
            prepareCatalogList();
        }
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

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rlCart.setVisibility(View.VISIBLE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rvCataloglist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        catalogListAdapter = new CatalogListAdapter(new OnItemClickListener() {
            @Override
            public void onClickAtPosition(View view, int catalogId) {
                CatalogActivity.start(CatalogListActivity.this, getCatalogById(catalogId)/*catalogs.get(position)*/);
            }

            @Override
            public void onClickWishList(View view, int catalogId) {
                Catalog catalog = getCatalogById(catalogId);
                if (!pref.isWishOrShareList(catalog, true)) {
                    ShareCatalogUtils.addWishOrSharedCatalog(apiService, CatalogListActivity.this, catalog, true);
                } else {
                    ShareCatalogUtils.deleteFromWishListAPI(apiService, CatalogListActivity.this, catalog);
                }
            }

            @Override
            public void onClickShareNow(View view, final int catalogId) {
                catalogId1 = catalogId;
                showSetMarginDialog();
            }
        });
        rvCataloglist.setAdapter(catalogListAdapter);
    }

    private void showSetMarginDialog() {
        String tag = "SetMarginDialog";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null)
            return;
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
        if (WhatsAppShareUtils.isWhatsAppInstalled(CatalogListActivity.this)) {
            Catalog catalog = getCatalogById(catalogId1);
            isWhatsappShare = true;
            if (ContextCompat.checkSelfPermission(CatalogListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                checkRuntimePermission(PERMISSION_REQUEST_WRITE_DATA);
            } else {
                ImageDownloadUtils.shareImages(this, catalog, null);
                if (!pref.isWishOrShareList(catalog, false)) {
                    ShareCatalogUtils.addWishOrSharedCatalog(apiService, CatalogListActivity.this, catalog, false);
                }
            }
        } else
            ViewUtils.showToast(CatalogListActivity.this, getString(R.string.whatsapp_not_installed));
    }

    private void checkRuntimePermission(final int request_id) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_title)
                    .setMessage(R.string.denied_permission)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CatalogListActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, request_id);
                        }
                    }).show();

        } else {
            if (pref.isFirstTimeAskingPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                pref.firstTimeAskingPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, false);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, request_id);
            } else {
                new AlertDialog.Builder(this)
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
                    ImageDownloadUtils.shareImages(this, catalog, null);
                    if (!pref.isWishOrShareList(catalog, false)) {
                        ShareCatalogUtils.addWishOrSharedCatalog(apiService, CatalogListActivity.this, catalog, false);
                    }
                }
                break;
            }
        }
    }

    private void prepareCatalogList() {
        showLoading();
        if (is_from.equals("Top_Catagory")) {
            apiService.getTopCategory(categoryName).enqueue(new Callback<TopCategory>() {
                @Override
                public void onResponse(@NonNull Call<TopCategory> call, @NonNull Response<TopCategory> response) {
                    TopCategoryData topCategoryData = response.body().getTopCategoryData();
                    if (response.isSuccessful() && topCategoryData != null) {
                        hideLoading();
                        catalogs = topCategoryData.getCategoryList();
                        catalogListAdapter.setCatalogList(catalogs);
                        if (catalogs.size() == 0)
                            tvEmptyCatalog.setVisibility(View.VISIBLE);
                        else {
                            llsort.setEnabled(true);
                            llfilter.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TopCategory> call, @NonNull Throwable t) {
                    hideLoading();
                    Toast.makeText(CatalogListActivity.this, getResources().getString(R.string.error_connection_failed), Toast.LENGTH_SHORT).show();
                    Log.d("MilanCheking", t.getMessage());
                    Log.d("MilanCheking", pref.getAccessToken());
                    Log.d("MilanCheking", pref.getFcmToken());
                }
            });
        } else {
            apiService.getCatalogList(collectionId).enqueue(new Callback<CollectionItemResponse>() {
                @Override
                public void onResponse(@NonNull Call<CollectionItemResponse> call, @NonNull Response<CollectionItemResponse> response) {
                    CollectionItemResponseData collectionItemResponseData = response.body().getCollectionItemResponseData();
                    if (response.isSuccessful() && collectionItemResponseData != null) {
                        hideLoading();
                        catalogs = collectionItemResponseData.getCataloglist();
                        Collections.sort(catalogs, new Comparator<Catalog>() {
                            @Override
                            public int compare(Catalog o1, Catalog o2) {
                                return o2.getCatalogCreationDate().compareTo(o1.getCatalogCreationDate());
                            }
                        });
                        catalogListAdapter.setCatalogList(catalogs);

                        if (catalogs.size() == 0)
                            tvEmptyCatalog.setVisibility(View.VISIBLE);
                        else {
                            llsort.setEnabled(true);
                            llfilter.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CollectionItemResponse> call, @NonNull Throwable t) {
                    hideLoading();
                    Toast.makeText(CatalogListActivity.this, getResources().getString(R.string.error_connection_failed), Toast.LENGTH_SHORT).show();
                    Log.d("MilanCheking", t.getMessage());
                    Log.d("MilanCheking", pref.getAccessToken());
                    Log.d("MilanCheking", pref.getFcmToken());
                }
            });
        }
    }

    @OnClick(R.id.llsort)
    public void onClickSort() {
        String tag = "SortBottomSheet";
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null)
            return;
        SortBottomSheet sortBottomSheet = SortBottomSheet.newInstance(checkedText);
        sortBottomSheet.show(ft, tag);
    }

    @OnClick(R.id.llfilter)
    public void onClickFilter() {
        if (filterList != null && filterList.size() == 0) {
            setCategory();
            setPrice();
            setRating();
            setDiscount();
        }

        String tag = "FilterBottomSheet";
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null)
            return;
        FilterBottomSheet filterBottomSheet = FilterBottomSheet.newInstance(filterList);
        filterBottomSheet.show(ft, tag);
    }

    private void setCategory() {
        categoryFilterList.clear();
        Map<String, Integer> categorisMap = new HashMap<>();

        for (Catalog catalog : catalogs) {
            String categoryName = catalog.getCategoryName();
            if (!categorisMap.containsKey(categoryName)) {
                categorisMap.put(categoryName, 0);
                FilterItem filterItem = new FilterItem(categoryName);
                categoryFilterList.add(filterItem);
            }
        }

        Filter categoryFilter = new Filter();
        categoryFilter.setFilterItems(categoryFilterList);
        categoryFilter.setMultiSelected(true);
        categoryFilter.setItemType(getString(R.string.title_category));
        filterList.add(categoryFilter);
    }

    private void setPrice() {
        priceFilterList.clear();
        for (String price : strPrice) {
            FilterItem filterItem = new FilterItem(price);
            priceFilterList.add(filterItem);
        }
        Filter priceFilter = new Filter();
        priceFilter.setFilterItems(priceFilterList);
        priceFilter.setMultiSelected(true);
        priceFilter.setItemType(getString(R.string.title_price));
        filterList.add(priceFilter);
    }

    private void setRating() {
        ratingFilterList.clear();
        for (String rating : strRating) {
            FilterItem filterItem = new FilterItem(rating);
            ratingFilterList.add(filterItem);
        }
        Filter ratingFilter = new Filter();
        ratingFilter.setFilterItems(ratingFilterList);
        ratingFilter.setMultiSelected(false);
        ratingFilter.setItemType(getString(R.string.title_rating));
        filterList.add(ratingFilter);
    }

    private void setDiscount() {
        discountFilterList.clear();
        String[] strDiscount = {"10% discount", "20% discount", "30% discount", "40% discount", "50% discount"};
        for (String discount : strDiscount) {
            FilterItem filterItem = new FilterItem(discount);
            discountFilterList.add(filterItem);
        }
        Filter discountFilter = new Filter();
        discountFilter.setFilterItems(discountFilterList);
        discountFilter.setMultiSelected(false);
        discountFilter.setItemType(getString(R.string.discount));
        filterList.add(discountFilter);
    }

    @OnClick(R.id.rlCart)
    public void onCartClick() {
        CartActivity.start(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collection_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();

        if (is_searchable) {
            showLoading();
            searchItem.expandActionView();
            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    onBackPressed();
                    return true;
                }
            });
        }

        searchView.setQueryHint(getString(R.string.search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (is_searchable) {
                    apiService.getHomeSearch(query).enqueue(new Callback<TopCategory>() {
                        @Override
                        public void onResponse(Call<TopCategory> call, Response<TopCategory> response) {
                            TopCategoryData topCategoryData = response.body().getTopCategoryData();
                            if (response.isSuccessful() && topCategoryData != null) {
                                hideLoading();
                                catalogs = topCategoryData.getCategoryList();
                                catalogListAdapter.setCatalogList(catalogs);

                                if (catalogs.size() == 0)
                                    tvEmptyCatalog.setVisibility(View.VISIBLE);
                                else {
                                    tvEmptyCatalog.setVisibility(View.GONE);
                                    llsort.setEnabled(true);
                                    llfilter.setEnabled(true);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<TopCategory> call, Throwable t) {
                            hideLoading();
                            Toast.makeText(CatalogListActivity.this, getResources().getString(R.string.error_connection_failed), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                catalogListAdapter.getFilter().filter(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                catalogListAdapter.getFilter().filter(query);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartBadgeCount();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isWhatsappShare) {
                    isWhatsappShare = false;
                    int price = getCatalogById(catalogId1).getCatalogBasePrice() + margin1;
                    String description = getString(R.string.title_cart_price) + " : " + price +
                            "\n" + getCatalogById(catalogId1).getCatalogDescription();
                    WhatsAppShareUtils.shareText(description, CatalogListActivity.this);
                }
            }
        }, 600);
        catalogListAdapter.notifyDataSetChanged();
    }

    private void setCartBadgeCount() {
        int count = PreferenceDataHelper.getInstance(this).getCartItemCount();
        if (count == 0) tvCartBadge.setVisibility(View.GONE);
        else {
            tvCartBadge.setVisibility(View.VISIBLE);
            tvCartBadge.setText(String.valueOf(count));
        }
    }

    public void showLoading() {
        progressWheel.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressWheel.setVisibility(View.GONE);
    }

    @Override
    public void onChecked(String text) {
        tvSortType.setText(text);
        this.checkedText = text;
        if (text.equals(getString(R.string.most_popular))) {
            Collections.sort(catalogListAdapter.getCatalogItems(), new Comparator<Catalog>() {
                @Override
                public int compare(Catalog o1, Catalog o2) {
                    return Integer.valueOf(o2.getOrderCount()) - Integer.valueOf(o1.getOrderCount());
                }
            });
        } else if (text.equals(getString(R.string.price_low_to_high))) {
            Collections.sort(catalogListAdapter.getCatalogItems(), new Comparator<Catalog>() {
                @Override
                public int compare(Catalog o1, Catalog o2) {
                    return o1.getCatalogBasePrice() - o2.getCatalogBasePrice();
                }
            });
        } else if (text.equals(getString(R.string.price_high_to_low))) {
            Collections.sort(catalogListAdapter.getCatalogItems(), new Comparator<Catalog>() {
                @Override
                public int compare(Catalog o1, Catalog o2) {
                    return o2.getCatalogBasePrice() - o1.getCatalogBasePrice();
                }
            });
        } else if (text.equals(getString(R.string.rating))) {
            Collections.sort(catalogListAdapter.getCatalogItems(), new Comparator<Catalog>() {
                @Override
                public int compare(Catalog o1, Catalog o2) {
                    return o2.getAverageRating().compareTo(o1.getAverageRating());
                }
            });
        } else {
            Collections.sort(catalogListAdapter.getCatalogItems(), new Comparator<Catalog>() {
                @Override
                public int compare(Catalog o1, Catalog o2) {
                    return o2.getCatalogCreationDate().compareTo(o1.getCatalogCreationDate());
                }
            });
        }

        //catalogListAdapter.setCatalogList(catalogs);
        catalogListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickApplyChangedListner(List<Filter> filterList) {
        this.filterList = filterList;
        List<String> catagoryList = new ArrayList<>();
        List<String> priceList = new ArrayList<>();
        String rating = null;
        List<Catalog> filteredList = new ArrayList<>();

        for (Filter filter : filterList) {
            for (FilterItem filterItem : filter.getFilterItems()) {
                if (filterItem.isSelected()) {
                    if (filter.getItemType().equals(getString(R.string.title_category))) {
                        catagoryList.add(filterItem.getName());
                    } else if (filter.getItemType().equals(getString(R.string.title_price))) {
                        priceList.add(filterItem.getName());
                    } else if (filter.getItemType().equals(getString(R.string.title_rating))) {
                        rating = filterItem.getName();
                    }
                }
            }
        }

        for (Catalog catalog : catalogs) {
            //if categoryList is empty then check for price directly O.W. check for category & price
            if (catagoryList.size() > 0) {
                if (catagoryList.contains(catalog.getCategoryName())) {
                    if (applyPriceFilter(priceList, rating, catalog)) {
                        filteredList.add(catalog);
                    }
                }
            } else {
                if (applyPriceFilter(priceList, rating, catalog))
                    filteredList.add(catalog);
            }
        }

        catalogListAdapter.setCatalogList(filteredList);
    }

    //check for price
    private boolean applyPriceFilter(List<String> priceList, String rating, Catalog catalog) {
        //if priceList is empty then check for rating directly O.W. check for price & rating
        if (priceList != null && priceList.size() > 0) {
            for (String price : priceList) {
                if (price.equals(strPrice[0]) && catalog.getCatalogBasePrice() > 0 && catalog.getCatalogBasePrice() < 250) {
                    return applyRatingFilter(rating, catalog);
                } else if (price.equals(strPrice[1]) && catalog.getCatalogBasePrice() >= 250 && catalog.getCatalogBasePrice() < 500) {
                    return applyRatingFilter(rating, catalog);
                } else if (price.equals(strPrice[2]) && catalog.getCatalogBasePrice() >= 500 && catalog.getCatalogBasePrice() < 750) {
                    return applyRatingFilter(rating, catalog);
                } else if (price.equals(strPrice[3]) && catalog.getCatalogBasePrice() >= 750) {
                    return applyRatingFilter(rating, catalog);
                }
            }
            return false;
        } else
            return applyRatingFilter(rating, catalog);
    }

    //check for rating
    private boolean applyRatingFilter(String rating, Catalog catalog) {
        return rating == null || rating.equals(strRating[4])
                && catalog.getAverageRating().equals("0") || !rating.equals(strRating[4])
                && Float.parseFloat(catalog.getAverageRating()) >= Float.valueOf(rating.replaceAll("[^\\d.]+|\\.(?!\\d)", ""));
    }

}
