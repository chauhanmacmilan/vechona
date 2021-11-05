package com.vechona.com.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vechona.app.R;
import com.vechona.com.ui.activity.CourierStatusTrackingActivity;
import com.vechona.com.ui.interfaces.OnOrderDetailsButtonClick;
import com.vechona.com.ui.model.Product;
import com.vechona.com.ui.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailsItemAdapter extends RecyclerView.Adapter<OrderDetailsItemAdapter.OrderDetailsItemHolder> {

    private List<Product> productList;
    private OnOrderDetailsButtonClick onOrderDetailsButtonClick;
    private String orderStatus;
    private Context context;

    public OrderDetailsItemAdapter(OnOrderDetailsButtonClick onOrderDetailsButtonClick, Context context) {
        this.onOrderDetailsButtonClick = onOrderDetailsButtonClick;
        this.context = context;
        productList = new ArrayList<>();
    }

    @NonNull
    @Override
    public OrderDetailsItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_order_details_layout, viewGroup, false);
        return new OrderDetailsItemHolder(view, onOrderDetailsButtonClick);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsItemHolder orderDetailsItemHolder, int i) {
        orderDetailsItemHolder.onBindViewHolder(productList.get(i));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setProductList(List<Product> productList, String orderStatus) {
        this.productList = productList;
        this.orderStatus = orderStatus;
        notifyDataSetChanged();
    }

    public class OrderDetailsItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivProductImage)
        AppCompatImageView ivProductImage;
        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvOrderPrice)
        TextView tvOrderPrice;
        @BindView(R.id.tvOrderQnty)
        TextView tvOrderQnty;
        @BindView(R.id.tvOrderSize)
        TextView tvOrderSize;
        @BindView(R.id.tvOrderColor)
        TextView tvOrderColor;
        @BindView(R.id.buttonTrack)
        Button buttonTrack;
        @BindView(R.id.buttonCancel)
        Button buttonCancel;
        @BindView(R.id.btnFeedback)
        Button buttonFeedback;
        @BindView(R.id.view)
        View view;

        private OrderDetailsItemHolder(@NonNull View itemView, OnOrderDetailsButtonClick onOrderDetailsButtonClick) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.buttonTrack)
        public void onTrackButtonClick() {
            onOrderDetailsButtonClick.onTrackButtonClick(getAdapterPosition());
            Intent i = new Intent(context, CourierStatusTrackingActivity.class);
            context.startActivity(i);
        }

        @OnClick(R.id.buttonCancel)
        public void onCancelButtonClick() {
            onOrderDetailsButtonClick.onCancelButtonClick(getAdapterPosition());
        }

        @OnClick(R.id.btnFeedback)
        public void onFeedbackButtonClick() {
            onOrderDetailsButtonClick.onFeedbackButtonClick(getAdapterPosition());
        }

        private void onBindViewHolder(Product product) {
            Glide.with(itemView.getContext()).load(product.getProductsImageUrls().get(0).getImageUrl()).into(ivProductImage);
            tvProductName.setText(StringUtils.camelCase(product.getProductName()));
            tvOrderPrice.setText(itemView.getContext().getResources().getString(R.string.priceFormat, product.getPrice()));
            if (orderStatus.equalsIgnoreCase("Cancel")) {
                buttonCancel.setVisibility(View.GONE);
                buttonFeedback.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            } else if (orderStatus.equals(itemView.getResources().getString(R.string.delivered))) {
                buttonFeedback.setVisibility(View.VISIBLE);
                buttonCancel.setVisibility(View.GONE);
            } else {
                buttonFeedback.setVisibility(View.GONE);
            }

            tvOrderColor.setText(itemView.getContext().getResources().getString(R.string.colorFormat, product.getColor()));
            tvOrderSize.setText(itemView.getContext().getResources().getString(R.string.sizeFormat, product.getSize()));
            tvOrderQnty.setText(itemView.getContext().getResources().getString(R.string.quantityFormat, product.getQuantity()));
        }

    }
}
