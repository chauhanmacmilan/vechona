package com.vechona.com.ui.activity;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
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
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
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
import com.vechona.com.data.remote.apiResponse.CartAddUpdateResponse;
import com.vechona.com.data.remote.apiResponse.ReviewResponse;
import com.vechona.com.data.remote.apiResponse.ReviewResponseData;
import com.vechona.com.ui.adapter.ItemImageAdapter;
import com.vechona.com.ui.adapter.ReviewAdapter;
import com.vechona.com.ui.fragment.CartItemAddDialog;
import com.vechona.com.ui.fragment.DiscardCartDialog;
import com.vechona.com.ui.fragment.SetMarginDialog;
import com.vechona.com.ui.interfaces.OnItemClickListener;
import com.vechona.com.ui.model.CartItem;
import com.vechona.com.ui.model.Product;
import com.vechona.com.ui.model.ProductStock;
import com.vechona.com.ui.model.ProductsImageUrl;
import com.vechona.com.ui.model.Review;
import com.vechona.com.ui.utils.BundleParams;
import com.vechona.com.ui.utils.ImageDownloadUtils;
import com.vechona.com.ui.utils.StringUtils;
import com.vechona.com.ui.utils.ViewUtils;
import com.vechona.com.ui.utils.WhatsAppShareUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemActivity extends AppCompatActivity implements CartItemAddDialog.CartItemAddDialogListener, DiscardCartDialog.DiscardCartDialogListener {

    private static final int PERMISSION_REQUEST_WRITE_DATA = 1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rlCart)
    RelativeLayout rlCart;

    @BindView(R.id.tvCartBadge)
    TextView tvCartBadge;

    @BindView(R.id.rvItemImage)
    RecyclerView rvItemImage;

    @BindView(R.id.tvProductName)
    TextView tvProductName;

    @BindView(R.id.tvDiscountablePrice)
    TextView tvDiscountablePrice;

    @BindView(R.id.tvProductPrice)
    TextView tvProductPrice;

    @BindView(R.id.tvDiscount)
    TextView tvDiscount;

    @BindView(R.id.llProductDetails)
    LinearLayout llProductDetails;

    @BindView(R.id.tvSoldBy)
    TextView tvSoldBy;

    @BindView(R.id.llButton)
    LinearLayout llButton;

    @BindView(R.id.llButtonCart)
    LinearLayout llButtonCart;

    @BindView(R.id.llCheckPincode)
    LinearLayout llCheckPinCode;

    @BindView(R.id.ivCheckPincode)
    AppCompatImageView ivCheckPincode;

    @BindView(R.id.tvCheckPincode)
    TextView tvCheckPincode;

    @BindView(R.id.rvProductReview)
    RecyclerView rvProductReview;

    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private ItemImageAdapter itemImageAdapter;
    private Product product;
    private ProductStock productStock;
    private List<ProductsImageUrl> productsImageUrls;
    private PreferenceDataHelper preferenceDataHelper;
    private ApiService apiService;
    private Map<String, Map<String, ProductStock>> stockMap;
    private boolean isWhatsappShare;
    private ReviewAdapter reviewAdapter;
    private int margin1;

    public static void start(Context context, Product product, int catalogId) {
        Intent intent = new Intent(context, ItemActivity.class);
        intent.putExtra(BundleParams.PRODUCT, product);
        intent.putExtra(BundleParams.CATALOG_ID, catalogId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @OnClick({R.id.llAddToCart, R.id.tvAddAnother})
    public void onAddCartClick() {
        List<ProductStock> productStockList = product.getProductStockList();
        ArrayList<String> sizeList = new ArrayList<>();
        ArrayList<String> colorList = new ArrayList<>();
        ArrayList<String> stockList = new ArrayList<>();

        for (ProductStock stock : productStockList) {
            if (!sizeList.contains(stock.getSize()))
                sizeList.add(stock.getSize());
            if (!colorList.contains(stock.getColor()))
                colorList.add(stock.getColor());
        }
        showSizeQntyChooser(product);
    }

    @OnClick(R.id.llCheckOut)
    public void onCheckOutClick() {
        CartActivity.start(ItemActivity.this);
    }


    @OnClick(R.id.btn_copy)
    public void onClickCopyButton() {
        StringBuilder product_details = new StringBuilder();
        product_details.append(product.getProductDescription());
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(product_details);
        ViewUtils.showToast(ItemActivity.this, getString(R.string.product_description));
    }

    @OnClick(R.id.llShareNow)
    public void onClickShareNow() {
        showSetMarginDialog();
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
        if (WhatsAppShareUtils.isWhatsAppInstalled(ItemActivity.this)) {
            isWhatsappShare = true;
            if (ContextCompat.checkSelfPermission(ItemActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                checkRuntimePermission(PERMISSION_REQUEST_WRITE_DATA);
            } else {
                ImageDownloadUtils.shareImages(ItemActivity.this, null, product);
            }
        } else
            ViewUtils.showToast(ItemActivity.this, getString(R.string.whatsapp_not_installed));
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
                            ActivityCompat.requestPermissions(ItemActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, request_id);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_WRITE_DATA: {
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    ImageDownloadUtils.shareImages(ItemActivity.this, null, product);
                }
                break;
            }
        }
    }

    @OnClick(R.id.btn_pincode)
    public void onClickEnterPincode() {
        Intent intent = new Intent(this, CheckPinCodeAvailabilityActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (data != null) {
                boolean is_availavle = data.getBooleanExtra(BundleParams.IS_AVAILABLE, false);
                String pincode = data.getStringExtra(BundleParams.PINCODE);
                llCheckPinCode.setVisibility(View.VISIBLE);
                if (is_availavle) {
                    ivCheckPincode.setImageDrawable(getResources().getDrawable(R.drawable.ic_checked));
                    tvCheckPincode.setText(getString(R.string.delivery_availability, pincode));
                    tvCheckPincode.setTextColor(getResources().getColor(R.color.green));
                } else {
                    ivCheckPincode.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));
                    tvCheckPincode.setText(getString(R.string.delivery_not_availability, pincode));
                    tvCheckPincode.setTextColor(getResources().getColor(R.color.red));
                }
            }

        }
    }

    private void showSizeQntyChooser(Product product) {
        String tag = "SizeQntyChooser";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null)
            return;
        CartItemAddDialog dialog = CartItemAddDialog.newInstance(product);
        dialog.show(fragmentTransaction, tag);
    }

    private void showDiscardCartDialog(String currentSupplier, String newSupplier) {
        String tag = "DiscardCartDialog";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DiscardCartDialog dialog = DiscardCartDialog.newInstance(currentSupplier, newSupplier);
        dialog.show(fragmentTransaction, tag);
    }

    private void initData() {
        preferenceDataHelper = PreferenceDataHelper.getInstance(ItemActivity.this);
        apiService = RemoteServiceHelper.createService(ApiService.class,
                RemoteServiceHelper.getRetrofitInstance(this));

        product = getIntent().getExtras().getParcelable(BundleParams.PRODUCT);
        product.setProductCatalogId(getIntent().getIntExtra(BundleParams.CATALOG_ID, -1));
        getSupportActionBar().setTitle(StringUtils.camelCase(product.getProductName()));
        productsImageUrls = product.getProductsImageUrls();
        stockMap = product.getProductStocks();
        initProduct();
        initReview();
    }

    private void initReview() {
        showLoading();
        apiService.getProductReviews(product.getProductId()).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                ReviewResponseData reviewResponseData = response.body().getReviewResponseData();
                if (response.isSuccessful() && reviewResponseData != null) {
                    List<Review> reviews = reviewResponseData.getReviews();
                    reviewAdapter.setReviews(reviews);
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                hideLoading();
            }
        });
    }

    private void initProduct() {
        tvProductName.setText(StringUtils.camelCase(product.getProductName()));
        int price;
        if (product.getDiscount() > 0) {
            price = product.getPrice() - (int) (product.getPrice() * product.getDiscount()) / 100;
            tvDiscountablePrice.setText(getString(R.string.priceFormat, price));
            tvProductPrice.setText(getString(R.string.priceFormat, product.getPrice()));
            tvProductPrice.setPaintFlags(tvProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvDiscount.setText(getString(R.string.title_discount, (int) product.getDiscount()));
        } else {
            tvDiscountablePrice.setText(getString(R.string.priceFormat, product.getPrice()));
            tvProductPrice.setVisibility(View.GONE);
            tvDiscount.setVisibility(View.GONE);
        }
        tvDiscount.setText(getString(R.string.title_discount, (int) product.getDiscount()));

        TextView productVariant = new TextView(ItemActivity.this);
        productVariant.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        productVariant.setText(product.getProductDescription());
        llProductDetails.addView(productVariant);

        tvSoldBy.setText(product.getVender());
        itemImageAdapter.setItems(productsImageUrls);
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

        itemImageAdapter = new ItemImageAdapter(new OnItemClickListener() {
            @Override
            public void onClickAtPosition(View view, int position) {
                ImageZoomActivity.start(ItemActivity.this, product, position);
            }

            @Override
            public void onClickWishList(View view, int position) {

            }

            @Override
            public void onClickShareNow(View view, int position) {

            }


        });

        rvItemImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        rvItemImage.setAdapter(itemImageAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvItemImage);

        reviewAdapter = new ReviewAdapter();
        rvProductReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvProductReview.setAdapter(reviewAdapter);
    }

    @Override
    public void onCartItemAddDialogDoneClick(ProductStock productStock, int qnty) {
        productStock.setQuantity(qnty);
        this.productStock = productStock;
        /*if (preferenceDataHelper.getCurrentCartSupplier() != null && !(product.getVender().equalsIgnoreCase(preferenceDataHelper.getCurrentCartSupplier())))
            showDiscardCartDialog(preferenceDataHelper.getCurrentCartSupplier(), product.getVender());*/
        addCart();
    }

    private void addCart() {
        showLoading();
        final CartItem cartItem = new CartItem();
        cartItem.setProductId(product.getProductId());
        cartItem.setCatalogId(product.getProductCatalogId());
        cartItem.setStockId(productStock.getStockId());
        cartItem.setQuantity(productStock.getQuantity());
        cartItem.setUserId(preferenceDataHelper.getUser().getUserID());
        cartItem.setProduct(product);
        cartItem.setStock(productStock);
        cartItem.setCategory(product.getCategory());
        cartItem.setWeight(product.getWeight());
        cartItem.setUnit(product.getUnit());

        apiService.addCart(cartItem).enqueue(new Callback<CartAddUpdateResponse>() {
            @Override
            public void onResponse(Call<CartAddUpdateResponse> call, Response<CartAddUpdateResponse> response) {
                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("success")) {
                    //preferenceDataHelper.setCurrentCartSupplier(product.getVender());
                    preferenceDataHelper.addCartItem(cartItem);
                    setCartBadgeCount();
                    llButton.setVisibility(View.GONE);
                    llButtonCart.setVisibility(View.VISIBLE);
                    Toast.makeText(ItemActivity.this, cartItem.getWeight()+ "  ---  "+ cartItem.getUnit(), Toast.LENGTH_SHORT).show();
                    hideLoading();
                    ViewUtils.showToast(ItemActivity.this, getString(R.string.msg_cart_added_successfully));
                }
            }

            @Override
            public void onFailure(Call<CartAddUpdateResponse> call, Throwable t) {
                hideLoading();
            }
        });
    }

    @Override
    public void onDiscardCartDialogDoneClick() {
        preferenceDataHelper.clearCart();
        addCart();
    }

    @OnClick(R.id.rlCart)
    public void onCartClick() {
        CartActivity.start(this);
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
                    int price = product.getPrice() + margin1;
                    String description = getString(R.string.title_cart_price) + " : " + price +
                            "\n" + product.getProductDescription();
                    WhatsAppShareUtils.shareText(description, ItemActivity.this);
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

    public void showLoading() {
        progressWheel.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressWheel.setVisibility(View.GONE);
    }
}
