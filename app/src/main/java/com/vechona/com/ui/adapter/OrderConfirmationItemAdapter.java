package com.vechona.com.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vechona.app.R;
import com.vechona.com.ui.model.CartItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderConfirmationItemAdapter extends RecyclerView.Adapter<OrderConfirmationItemAdapter.OrderConfirmationItemHolder> {

    private Context context;
    private List<CartItem> cartItemList;

    public OrderConfirmationItemAdapter(Context context) {
        this.context = context;
        cartItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public OrderConfirmationItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_order_confirmation_layout, viewGroup, false);
        return new OrderConfirmationItemAdapter.OrderConfirmationItemHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderConfirmationItemHolder orderConfirmationItemHolder, int i) {
        orderConfirmationItemHolder.onBindViewHolder(cartItemList.get(i));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
        notifyDataSetChanged();
    }

    public class OrderConfirmationItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivProductImage)
        AppCompatImageView ivProductImage;
        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvProductPrice)
        TextView tvProductPrice;
        @BindView(R.id.tvProductSize)
        TextView tvProductSize;
        @BindView(R.id.tvProductColor)
        TextView tvProductColor;
        @BindView(R.id.tvProductQnty)
        TextView tvProductQnty;

        private Context context;

        private OrderConfirmationItemHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        void onBindViewHolder(CartItem cartItem) {
            Glide.with(itemView.getContext())
                    .load(cartItem.getProduct().getProductsImageUrls().get(0).getImageUrl())
                    .into(ivProductImage);
            tvProductName.setText(cartItem.getProduct().getProductName());
            int price;
            if (cartItem.getProduct().getDiscount() > 0) {
                price = cartItem.getProduct().getPrice() - (int) (cartItem.getProduct().getPrice() * cartItem.getProduct().getDiscount()) / 100;
            } else
                price = cartItem.getProduct().getPrice();
            tvProductPrice.setText(itemView.getContext().getString(R.string.priceFormat, price));
            tvProductSize.setText(itemView.getContext().getString(R.string.sizeFormat, cartItem.getStock().getSize()));
            tvProductColor.setText(itemView.getContext().getString(R.string.colorFormat, cartItem.getStock().getColor()));
            tvProductQnty.setText(itemView.getContext().getString(R.string.quantityFormat, String.valueOf(cartItem.getQuantity())));
        }
    }
}
