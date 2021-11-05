package com.vechona.com.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vechona.app.R;
import com.vechona.com.ui.interfaces.ItemClickListener;
import com.vechona.com.ui.model.UserOrderItem;
import com.vechona.com.ui.utils.DateFormatUtils;
import com.vechona.com.ui.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemHolder> {

    private ItemClickListener itemClickListener;
    private List<UserOrderItem> userOrderItemList;

    public OrderItemAdapter(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        userOrderItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public OrderItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_order_layout, viewGroup, false);
        return new OrderItemHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemHolder orderItemHolder, int i) {
        orderItemHolder.onBindViewHolder(userOrderItemList.get(i));
    }

    @Override
    public int getItemCount() {
        return userOrderItemList.size();
    }

    public void setUserOrderItemList(List<UserOrderItem> userOrderItemList) {
        this.userOrderItemList = userOrderItemList;
        notifyDataSetChanged();
    }

    public class OrderItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivOrderImage)
        AppCompatImageView ivOrderImage;
        @BindView(R.id.tvOrderId)
        TextView tvOrderId;
        @BindView(R.id.tvOrderProductName)
        TextView tvOrderProductName;
        @BindView(R.id.tvOrderPrice)
        TextView tvOrderPrice;
        @BindView(R.id.tvOrderName)
        TextView tvOrderName;
        @BindView(R.id.tvOrderDate)
        TextView tvOrderDate;

        private OrderItemHolder(@NonNull View itemView, final ItemClickListener itemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }

        void onBindViewHolder(UserOrderItem userOrderItem) {
            Glide.with(itemView.getContext()).load(userOrderItem.getProductList().get(0).getProductsImageUrls().get(0).getImageUrl()).into(ivOrderImage);
            tvOrderId.setText(itemView.getResources().getString(R.string.title_order_no, userOrderItem.getOrderId()));
            tvOrderDate.setText(DateFormatUtils.getDateWithYear(userOrderItem.getOrderCreationDate()));
            tvOrderPrice.setText(itemView.getContext().getResources().getString(R.string.priceFormat, Integer.parseInt(userOrderItem.getTotalAmount())));
            tvOrderName.setText(StringUtils.camelCase(userOrderItem.getCustomerName()));
            tvOrderProductName.setText(StringUtils.camelCase(userOrderItem.getProductList().get(0).getProductName()));
        }
    }
}
