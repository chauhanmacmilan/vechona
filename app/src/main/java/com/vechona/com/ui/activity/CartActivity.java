package com.vechona.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.CartAddUpdateResponse;
import com.vechona.com.ui.adapter.CartItemAdapter;
import com.vechona.com.ui.fragment.RemoveCartItemDialog;
import com.vechona.com.ui.interfaces.OnCartItemClickListener;
import com.vechona.com.ui.model.CartItem;
import com.vechona.com.ui.model.DeleteCart;
import com.vechona.com.ui.model.ProductStock;
import com.vechona.com.ui.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RemoveCartItemDialog.RemoveCartItemDialogListener {

    @BindView(R.id.tvCartToolbarTotalPrice)
    TextView tvCartToolbarTotalPrice;
    @BindView(R.id.tvCartToolbarTotalItem)
    TextView tvCartToolbarTotalItem;

    @BindView(R.id.radioGroupPayment)
    RadioGroup radioGroupPayment;

    @BindView(R.id.radioCOD)
    RadioButton radioCOD;
    @BindView(R.id.tvSupplierName)
    TextView tvSupplierName;
    @BindView(R.id.tvProductCharges)
    TextView tvProductCharges;
    @BindView(R.id.tvCODCharges)
    TextView tvCODCharges;
    @BindView(R.id.tvCartPrice)
    TextView tvCartPrice;
    @BindView(R.id.tvShippingCharges)
    TextView tvShippingCharges;
    @BindView(R.id.tvCartTotalPrice)
    TextView tvCartTotalPrice;
    @BindView(R.id.llCODCharges)
    LinearLayout llCODCharges;
    @BindView(R.id.rvCart)
    RecyclerView rvCart;
    @BindView(R.id.llButtonProceed)
    LinearLayout llButtonProceed;
    @BindView(R.id.llCartEmptyView)
    LinearLayout llCartEmptyView;
    @BindView(R.id.cart)
    NestedScrollView cart;
    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private CartItemAdapter cartItemAdapter;
    private List<CartItem> cartItemList;
    private ApiService apiService;
    private PreferenceDataHelper preferenceDataHelper;

    public static void start(Context context) {
        Intent intent = new Intent(context, CartActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @OnClick(R.id.llButtonProceed)
    public void onButtonProceedClick() {

        int selectedId = radioGroupPayment.getCheckedRadioButtonId();
        int we = getWeightFromCart();
        // find the radiobutton by returned id
        RadioButton radioButton = findViewById(selectedId);
        ShippingAddressActivity.start(CartActivity.this, radioButton.getText().toString(), we);
    }

    @OnClick(R.id.ivShippingInfo)
    public void onShippingInfoClick() {

    }

    @OnClick(R.id.btnBrowseCatalogs)
    public void onBrowseCatalogsClick() {
        HomeActivity.start(CartActivity.this);
        finish();
    }

    @OnClick(R.id.ivCartToolbarBack)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initView() {
        cartItemAdapter = new CartItemAdapter(CartActivity.this, new OnCartItemClickListener() {
            @Override
            public void onRemoveItemClick(int position) {
                showRemoveItemDialog(position);
            }

            @Override
            public void onSizeSpinnerClick(ProductStock productStock, int position) {
                sizeChange(productStock, position);
            }

            @Override
            public void onQntyMinusClick(int position) {
                decreaseQnty(position);
            }

            @Override
            public void onQntyPlusClick(int position) {
                increaseQnty(position);
            }
        });
        rvCart.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        rvCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCart.setAdapter(cartItemAdapter);
    }

    private void sizeChange(ProductStock productStock, int position) {
        cartItemList.get(position).setStock(productStock);
        updateCartItem(position);
    }

    private void initData() {
        apiService = RemoteServiceHelper.createService(ApiService.class,
                RemoteServiceHelper.getRetrofitInstance(this));
        preferenceDataHelper = PreferenceDataHelper.getInstance(CartActivity.this);
        prepareCartList();
    }

    private void prepareCartList() {
        if (preferenceDataHelper.isCOdEnable())
            radioCOD.setVisibility(View.VISIBLE);

        radioGroupPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioCOD:
                        llCODCharges.setVisibility(View.VISIBLE);
                        setUpPrices(true);
                        break;

                    case R.id.radioOnline:
                        llCODCharges.setVisibility(View.GONE);
                        setUpPrices(false);
                        break;

                    default:
                        break;
                }
            }
        });

        cartItemList = preferenceDataHelper.getCartList();
        if (cartItemList != null && cartItemList.size() > 0) {
            showEmptyView(false);
            setAdapterList();
        } else showEmptyView(true);
    }

    private void showEmptyView(boolean b) {
        if (b) {
            cart.setVisibility(View.GONE);
            llButtonProceed.setVisibility(View.GONE);
            tvCartToolbarTotalItem.setVisibility(View.GONE);
            tvCartToolbarTotalPrice.setText(getString(R.string.title_cart));
            llCartEmptyView.setVisibility(View.VISIBLE);
        } else {
            cart.setVisibility(View.VISIBLE);
            llButtonProceed.setVisibility(View.VISIBLE);
            tvCartToolbarTotalItem.setVisibility(View.VISIBLE);
            llCartEmptyView.setVisibility(View.GONE);
        }
    }

    private void setAdapterList() {
        tvSupplierName.setText(cartItemList.get(0).getProduct().getVender());
        cartItemAdapter.setCartItemList(cartItemList);
        setUpPrices(radioCOD.isChecked());
    }

    private void setUpPrices(boolean isCod) {
        int productCharge = 0;
        int itemCount = 0;
        int shippingCharge = 0;
        for (CartItem cartItem : cartItemList) {
            int price;
            if (cartItem.getProduct().getDiscount() > 0) {
                price = cartItem.getProduct().getPrice() - (int) (cartItem.getProduct().getPrice() * cartItem.getProduct().getDiscount()) / 100;
            } else
                price = cartItem.getProduct().getPrice();
            productCharge += price * cartItem.getQuantity();
            itemCount += cartItem.getQuantity();
            shippingCharge += cartItem.getCategory().getShippingCharges();
        }

        int cartPrice;
        int CODCharge = isCod ? 10 : 0;
        cartPrice = productCharge + CODCharge;
        int totalPrice = cartPrice + shippingCharge;
        tvProductCharges.setText(getString(R.string.priceFormat, productCharge));
        tvCODCharges.setText(getString(R.string.plusPriceFormat, CODCharge));
        tvCartPrice.setText(getString(R.string.priceFormat, cartPrice));
        tvShippingCharges.setText(getString(R.string.plusPriceFormat, shippingCharge));
        tvCartTotalPrice.setText(getString(R.string.priceFormat, totalPrice));
        tvCartToolbarTotalPrice.setText(getString(R.string.toolbarPriceFormat, totalPrice));
        tvCartToolbarTotalItem.setText(getResources().getQuantityString(R.plurals.toolbarItemFormat, itemCount, itemCount));
    }
    private int getWeightFromCart(){
        int weight = 0;
        for (int i =0; i < cartItemList.size(); i++) {
            int quntity = cartItemList.get(i).getQuantity();
            int getWeight = Integer.valueOf(cartItemList.get(i).getProduct().getWeight());
            int totalWeight = getWeight * quntity;
            weight = weight + totalWeight;
        }
        return weight;
    }

    private void decreaseQnty(int position) {
        int qnty = cartItemList.get(position).getQuantity();
        if (qnty == 1)
            showRemoveItemDialog(position);
        else {
            cartItemList.get(position).setQuantity(--qnty);
            cartItemAdapter.notifyItemChanged(position);
            updateCartItem(position);
        }
    }

    private void updateCartItem(final int position) {
        showLoading();
        apiService.updateCart(cartItemList.get(position)).enqueue(new Callback<CartAddUpdateResponse>() {
            @Override
            public void onResponse(Call<CartAddUpdateResponse> call, Response<CartAddUpdateResponse> response) {
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    hideLoading();
                    preferenceDataHelper.saveCartList(cartItemList);
                    setUpPrices(radioCOD.isChecked());
                }
            }

            @Override
            public void onFailure(Call<CartAddUpdateResponse> call, Throwable t) {
                hideLoading();
            }
        });
    }

    private void increaseQnty(int position) {
        int qnty = cartItemList.get(position).getQuantity();
        if (qnty == cartItemList.get(position).getStock().getQuantity())
            ViewUtils.showToast(CartActivity.this, getString(R.string.msg_qnty_out_of_stock));
        else {
            cartItemList.get(position).setQuantity(++qnty);
            cartItemAdapter.notifyItemChanged(position);
            updateCartItem(position);
            //setUpPrices();
        }
    }

    private void showRemoveItemDialog(int position) {
        String tag = "RemoveItemDialog";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null)
            return;
        RemoveCartItemDialog dialog = RemoveCartItemDialog.newInstance(position,
                cartItemList.get(position).getProduct().getProductName(),
                cartItemList.get(position).getStock().getSize());
        dialog.show(fragmentTransaction, tag);
    }

    private void removeItem(final int position) {
        showLoading();
        List<CartItem> cart = new ArrayList<>();
        cart.add(cartItemList.get(position));
        DeleteCart deleteCart = new DeleteCart(cart);
        apiService.deleteCart(deleteCart)
                .enqueue(new Callback<CartAddUpdateResponse>() {
                    @Override
                    public void onResponse(Call<CartAddUpdateResponse> call, Response<CartAddUpdateResponse> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase("success")) {
                                hideLoading();
                                preferenceDataHelper.removeCartItem(position);
                                cartItemList.remove(position);
                                ViewUtils.showToast(CartActivity.this, getString(R.string.msg_cart_updated_successfully));
                                cartItemAdapter.notifyItemRemoved(position);
                                if (cartItemList.size() == 0)
                                    prepareCartList();
                                setUpPrices(radioCOD.isChecked());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CartAddUpdateResponse> call, Throwable t) {
                        hideLoading();
                    }
                });
    }

    @Override
    public void onRemoveCartItemDialogDoneClick(int position) {
        removeItem(position);
    }

    public void showLoading() {
        progressWheel.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressWheel.setVisibility(View.GONE);
    }
}
