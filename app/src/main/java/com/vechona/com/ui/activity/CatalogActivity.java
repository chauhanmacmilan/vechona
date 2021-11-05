package com.vechona.com.ui.activity;

import android.Manifest;
import android.content.ClipboardManager;
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
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.vechona.app.BuildConfig;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.ReviewResponse;
import com.vechona.com.data.remote.apiResponse.ReviewResponseData;
import com.vechona.com.ui.adapter.CatalogAdapter;
import com.vechona.com.ui.adapter.ReviewAdapter;
import com.vechona.com.ui.fragment.SetMarginDialog;
import com.vechona.com.ui.interfaces.OnItemClickListener;
import com.vechona.com.ui.model.Catalog;
import com.vechona.com.ui.model.CatalogVariant;
import com.vechona.com.ui.model.Product;
import com.vechona.com.ui.model.Review;
import com.vechona.com.ui.utils.BundleParams;
import com.vechona.com.ui.utils.DateFormatUtils;
import com.vechona.com.ui.utils.ImageDownloadUtils;
import com.vechona.com.ui.utils.ShareCatalogUtils;
import com.vechona.com.ui.utils.StringUtils;
import com.vechona.com.ui.utils.ViewUtils;
import com.vechona.com.ui.utils.WhatsAppShareUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_WRITE_DATA = 1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rlCart)
    RelativeLayout rlCart;

    @BindView(R.id.tvCartBadge)
    TextView tvCartBadge;

    @BindView(R.id.ivWishlist)
    AppCompatImageView ivWishlist;

    @BindView(R.id.tvWishlist)
    TextView tvWishlist;

    @BindView(R.id.rvCatalog)
    RecyclerView rvCatalog;

    @BindView(R.id.catalog_title)
    TextView catalogTitle;

    @BindView(R.id.llcatalogRating)
    LinearLayout llCatalogRating;

    @BindView(R.id.catalogRating)
    TextView catalogRating;

    @BindView(R.id.llCatalogVariants)
    LinearLayout llCatalogVariants;

    @BindView(R.id.rvCatalogReview)
    RecyclerView rvCatalogReview;

    @BindView(R.id.tvPrebook)
    TextView tvPrebook;

    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private CatalogAdapter catalogAdapter;
    private Catalog catalog;
    private ApiService apiService;
    private boolean isWhatsappShare;
    private int position1;
    private boolean shareCatalog;
    private PreferenceDataHelper preferenceDataHelper;
    private ReviewAdapter reviewAdapter;
    private int margin1;

    public static void start(Context context, Catalog catalog) {
        Intent intent = new Intent(context, CatalogActivity.class);
        intent.putExtra(BundleParams.CATALOG, catalog);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        apiService = RemoteServiceHelper.createService(ApiService.class,
                RemoteServiceHelper.getRetrofitInstance(this));

        preferenceDataHelper = PreferenceDataHelper.getInstance(this);
        catalog = getIntent().getExtras().getParcelable(BundleParams.CATALOG);
        getSupportActionBar().setTitle(StringUtils.camelCase(catalog.getCatalogName()));
        if (preferenceDataHelper.isWishOrShareList(catalog, true)) {
            ivWishlist.setImageResource(R.drawable.ic_favorite);
            tvWishlist.setText(R.string.wishlisted);
        } else {
            ivWishlist.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            tvWishlist.setText(R.string.wishlist);
        }
        initProductList();
        initCatalogReview();
    }

    private void initCatalogReview() {
        showLoading();
        apiService.getCatalogReviews(catalog.getCatalogId()).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                ReviewResponseData reviewResponseData = response.body().getReviewResponseData();
                if (response.isSuccessful() && reviewResponseData != null) {
                    hideLoading();
                    List<Review> catalogReviews = reviewResponseData.getReviews();
                    reviewAdapter.setReviews(catalogReviews);
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                hideLoading();
            }
        });
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
        rlCart.setVisibility(View.VISIBLE);

        rvCatalog.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        catalogAdapter = new CatalogAdapter(new OnItemClickListener() {
            @Override
            public void onClickAtPosition(View view, int position) {
                ItemActivity.start(CatalogActivity.this, catalog.getProducts().get(position), catalog.getCatalogId());
            }


            @Override
            public void onClickWishList(View view, int position) {

            }

            @Override
            public void onClickShareNow(View view, int position) {
                if (WhatsAppShareUtils.isWhatsAppInstalled(CatalogActivity.this)) {
                    isWhatsappShare = true;
                    position1 = position;
                    showSetMarginDialog();
                } else
                    ViewUtils.showToast(CatalogActivity.this, getString(R.string.whatsapp_not_installed));
            }
        });
        rvCatalog.setAdapter(catalogAdapter);

        reviewAdapter = new ReviewAdapter();
        rvCatalogReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCatalogReview.setAdapter(reviewAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_WRITE_DATA: {
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (shareCatalog) {
                        ImageDownloadUtils.shareImages(CatalogActivity.this, catalog, null);
                    } else {
                        Product product = catalog.getProducts().get(position1);
                        ImageDownloadUtils.shareImages(CatalogActivity.this, null, product);
                    }
                }
                break;
            }
        }
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
                            ActivityCompat.requestPermissions(CatalogActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, request_id);
                        }
                    }).show();

        } else {
            if (preferenceDataHelper.isFirstTimeAskingPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                preferenceDataHelper.firstTimeAskingPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, false);
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

    @OnClick(R.id.llwishlist)
    public void onClickWishList() {
        if (preferenceDataHelper.isWishOrShareList(catalog, true)) {
            ShareCatalogUtils.deleteFromWishListAPI(apiService, CatalogActivity.this, catalog);
            ivWishlist.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            tvWishlist.setText(R.string.wishlist);
        } else {
            ShareCatalogUtils.addWishOrSharedCatalog(apiService, CatalogActivity.this, catalog, true);
            ivWishlist.setImageResource(R.drawable.ic_favorite);
            tvWishlist.setText(R.string.wishlisted);
        }

    }

    @OnClick(R.id.rlCart)
    public void onCartClick() {
        CartActivity.start(this);
    }

    @OnClick(R.id.llcopy)
    public void onClickCopy() {
        String text = catalogTitle.getText().toString() + "\n" + catalog.getCatalogDescription() + "\n";
        CatalogVariant catalogVariant = catalog.getCatalogVariant();

        if (catalogVariant != null) {
            text += catalogVariant.getVarientName() + " : " + catalogVariant.getVariantValue();
        }
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(text);
        ViewUtils.showToast(CatalogActivity.this, getString(R.string.catalog_description));
    }

    @OnClick(R.id.share_whatsapp)
    public void onClickShareCatalog() {
        if (WhatsAppShareUtils.isWhatsAppInstalled(CatalogActivity.this)) {
            shareCatalog = true;
            showSetMarginDialog();
        } else
            ViewUtils.showToast(CatalogActivity.this, getString(R.string.whatsapp_not_installed));
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

    private void initProductList() {
        catalogTitle.setText(StringUtils.camelCase(catalog.getCatalogName()));
        if (catalog.is_prepaid()) {
            tvPrebook.setText(getString(R.string.title_shipping_available) + " " + DateFormatUtils.getDate(catalog.getPreShippingDate()));
            tvPrebook.setVisibility(View.VISIBLE);
        }

        if (!catalog.getAverageRating().equals("0")) {
            llCatalogRating.setVisibility(View.VISIBLE);
            catalogRating.setText(String.format("%.1f", Float.parseFloat(catalog.getAverageRating())));
        } else llCatalogRating.setVisibility(View.GONE);


        TextView discription = new TextView(CatalogActivity.this);
        discription.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        discription.setText(catalog.getCatalogDescription());
        llCatalogVariants.addView(discription);
        CatalogVariant catalogVariant = catalog.getCatalogVariant();

        if (catalogVariant != null) {
            TextView tvCatalog = new TextView(CatalogActivity.this);
            tvCatalog.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tvCatalog.setText(catalogVariant.getVarientName() + " : " + catalogVariant.getVariantValue());
            llCatalogVariants.addView(tvCatalog);
        }

        List<Product> productLists = catalog.getProducts();
        catalogAdapter.setCatalog(productLists);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartBadgeCount();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int price;
                if (isWhatsappShare) {
                    isWhatsappShare = false;
                    price = catalog.getProducts().get(position1).getPrice() + margin1;
                    WhatsAppShareUtils.shareText(getString(R.string.title_cart_price) + " : " + price +
                            "\n" + catalog.getProducts().get(position1).getProductDescription(), CatalogActivity.this);
                }
                if (shareCatalog) {
                    shareCatalog = false;
                    price = catalog.getCatalogBasePrice() + margin1;
                    WhatsAppShareUtils.shareText(getString(R.string.title_cart_price) + " : " + price +
                            "\n" + catalog.getCatalogDescription(), CatalogActivity.this);
                }

            }
        }, 600);
    }

    private void setCartBadgeCount() {
        int count = PreferenceDataHelper.getInstance(this).getCartItemCount();
        if (count == 0) tvCartBadge.setVisibility(View.GONE);
        else {
            tvCartBadge.setVisibility(View.VISIBLE);
            tvCartBadge.setText(String.valueOf(count));
        }
    }

    private void shareWhatsappImage() {

        if (ContextCompat.checkSelfPermission(CatalogActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            checkRuntimePermission(PERMISSION_REQUEST_WRITE_DATA);
        } else {
            if (catalog.getProducts().size() > 0) {
                if (shareCatalog) {
                    ImageDownloadUtils.shareImages(CatalogActivity.this, catalog, null);
                } else {
                    Product product = catalog.getProducts().get(position1);
                    ImageDownloadUtils.shareImages(CatalogActivity.this, null, product);
                }
                if (!preferenceDataHelper.isWishOrShareList(catalog, false)) {
                    ShareCatalogUtils.addWishOrSharedCatalog(apiService, CatalogActivity.this, catalog, false);
                }
            }
        }
    }

    public void showLoading() {
        progressWheel.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressWheel.setVisibility(View.GONE);
    }
}
