package com.vechona.com.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vechona.app.R;
import com.vechona.com.ui.interfaces.RecyclerViewItemClickListener;
import com.vechona.com.ui.model.ShippingAddressItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShippingAddressItemAdapter extends RecyclerView.Adapter<ShippingAddressItemAdapter.ShippingAddressItemHolder> {

    private List<ShippingAddressItem> addressItemList;
    private RecyclerViewItemClickListener recyclerViewItemClickListener;
    private int selectedRadioPostion;

    public ShippingAddressItemAdapter(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
        addressItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ShippingAddressItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_shipping_address_layout, viewGroup, false);
        return new ShippingAddressItemHolder(itemView, recyclerViewItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShippingAddressItemHolder shippingAddressItemHolder, int i) {
        shippingAddressItemHolder.onBindViewHolder(addressItemList.get(i));
        if (addressItemList.get(i).isSelected()) selectedRadioPostion = i;
    }

    @Override
    public int getItemCount() {
        return addressItemList.size();
    }

    public void setAddressItemList(List<ShippingAddressItem> addressItemList) {
        this.addressItemList = addressItemList;
        notifyDataSetChanged();
    }

    public class ShippingAddressItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.tvPhoneNumber)
        TextView tvPhoneNumber;
        @BindView(R.id.radio)
        RadioButton radioButton;

        private RecyclerViewItemClickListener recyclerViewItemClickListener;

        private ShippingAddressItemHolder(@NonNull View itemView, RecyclerViewItemClickListener recyclerViewItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.recyclerViewItemClickListener = recyclerViewItemClickListener;
        }

        @OnClick(R.id.radio)
        public void onRadioClick() {
            recyclerViewItemClickListener.onRadioClick(getAdapterPosition(), selectedRadioPostion);
            selectedRadioPostion = getAdapterPosition();
        }

        @OnClick(R.id.ivEdit)
        public void onEditClick() {
            recyclerViewItemClickListener.onItemClick(getAdapterPosition());
        }

        void onBindViewHolder(ShippingAddressItem shippingAddressItem) {
            tvName.setText(shippingAddressItem.getName());
            tvAddress.setText(shippingAddressItem.getAddress());
            tvPhoneNumber.setText(shippingAddressItem.getPhoneNumber());
            radioButton.setChecked(shippingAddressItem.isSelected());
        }
    }
}