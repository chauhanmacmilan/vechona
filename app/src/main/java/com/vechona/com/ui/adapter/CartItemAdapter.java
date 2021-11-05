package com.vechona.com.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vechona.app.R;
import com.vechona.com.ui.interfaces.OnCartItemClickListener;
import com.vechona.com.ui.model.CartItem;
import com.vechona.com.ui.model.ProductStock;
import com.vechona.com.ui.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ItemHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private OnCartItemClickListener onCartItemClickListener;

    public CartItemAdapter(Context context, OnCartItemClickListener onCartItemClickListener) {
        this.context = context;
        this.onCartItemClickListener = onCartItemClickListener;
        cartItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_cart_layout, viewGroup, false);
        return new ItemHolder(itemView, context, onCartItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
        itemHolder.onBindViewHolder(cartItemList.get(i));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivProductImage)
        ImageView ivProductImage;
        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.ivDeleteItem)
        AppCompatImageView ivDeleteItem;
        @BindView(R.id.tvProductPrice)
        TextView tvProductPrice;
        @BindView(R.id.spinnerSize)
        Spinner spinnerSize;
        @BindView(R.id.spinnerColor)
        Spinner spinnerColor;
        @BindView(R.id.ivMinusQnty)
        AppCompatImageView ivMinusQnty;
        @BindView(R.id.tvQnty)
        TextView tvQnty;
        @BindView(R.id.ivPlusQnty)
        AppCompatImageView ivPlusQnty;
        @BindView(R.id.tvDiscountablePrice)
        TextView tvDiscountablePrice;

        private OnCartItemClickListener onCartItemClickListener;
        private Map<String, Map<String, ProductStock>> stockMap;
        private List<String> sizes;
        private List<String> colors;

        private ItemHolder(@NonNull View itemView, final Context context, final OnCartItemClickListener onCartItemClickListener) {
            super(itemView);
            this.onCartItemClickListener = onCartItemClickListener;
            ButterKnife.bind(this, itemView);

            spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    colors = new ArrayList<>(stockMap.get(sizes.get(spinnerSize.getSelectedItemPosition())).keySet());
                    spinnerColor.setAdapter(new ArrayAdapter<String>(context, R.layout.item_size_spinner_layout, colors));
                    onCartItemClickListener.onSizeSpinnerClick(
                            stockMap.get(sizes.get(spinnerSize.getSelectedItemPosition()))
                                    .get(colors.get(0))
                            , getAdapterPosition());

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    onCartItemClickListener.onSizeSpinnerClick(
                            stockMap.get(sizes.get(spinnerSize.getSelectedItemPosition()))
                                    .get(colors.get(spinnerColor.getSelectedItemPosition()))
                            , getAdapterPosition());

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        @OnClick(R.id.ivDeleteItem)
        public void onDeleteItemClick() {
            onCartItemClickListener.onRemoveItemClick(getAdapterPosition());
        }

        @OnClick(R.id.ivMinusQnty)
        public void onQntyMinusClick() {
            onCartItemClickListener.onQntyMinusClick(getAdapterPosition());
        }

        @OnClick(R.id.ivPlusQnty)
        public void onQntyPlusClick() {
            onCartItemClickListener.onQntyPlusClick(getAdapterPosition());
        }

        void onBindViewHolder(CartItem cartItem) {
            Glide.with(itemView.getContext())
                    .load(cartItem.getProduct().getProductsImageUrls().get(0).getImageUrl())
                    .into(ivProductImage);

            int price;
            if (cartItem.getProduct().getDiscount() > 0) {
                price = cartItem.getProduct().getPrice() - (int) (cartItem.getProduct().getPrice() * cartItem.getProduct().getDiscount()) / 100;
                tvDiscountablePrice.setText(context.getString(R.string.priceFormat, price));
                tvProductPrice.setText(context.getString(R.string.priceFormat, cartItem.getProduct().getPrice()));
                tvProductPrice.setPaintFlags(tvProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                tvDiscountablePrice.setText(context.getString(R.string.priceFormat, cartItem.getProduct().getPrice()));
                tvProductPrice.setVisibility(View.GONE);
            }

            tvQnty.setText(String.valueOf(cartItem.getQuantity()));
            tvProductName.setText(StringUtils.camelCase(cartItem.getProduct().getProductName()));

            stockMap = cartItem.getProduct().getProductStocks();
            sizes = new ArrayList<>(stockMap.keySet());
            spinnerSize.setAdapter(new ArrayAdapter<String>(context, R.layout.item_size_spinner_layout, sizes));

            colors = new ArrayList<>(stockMap.get(sizes.get(spinnerSize.getSelectedItemPosition())).keySet());
            spinnerColor.setAdapter(new ArrayAdapter<String>(context, R.layout.item_size_spinner_layout, colors));

            spinnerSize.setSelection(sizes.indexOf(cartItem.getStock().getSize()));
            spinnerColor.setSelection(colors.indexOf(cartItem.getStock().getColor()));
        }
    }
}
