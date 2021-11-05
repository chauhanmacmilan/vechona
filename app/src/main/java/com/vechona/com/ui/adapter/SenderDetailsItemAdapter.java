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
import com.vechona.com.ui.model.SenderDetailsItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SenderDetailsItemAdapter extends RecyclerView.Adapter<SenderDetailsItemAdapter.SenderDetailsItemHolder> {

    private RecyclerViewItemClickListener recyclerViewItemClickListener;
    private List<SenderDetailsItem> detailsItemList;
    private int selectedRadioPostion;

    public SenderDetailsItemAdapter(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
        detailsItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public SenderDetailsItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_sender_details_layout, viewGroup, false);
        return new SenderDetailsItemHolder(itemView, recyclerViewItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SenderDetailsItemHolder senderDetailsItemHolder, int i) {
        senderDetailsItemHolder.onBindViewHolder(detailsItemList.get(i));
        if (detailsItemList.get(i).isSelected()) selectedRadioPostion = i;
    }

    @Override
    public int getItemCount() {
        return detailsItemList.size();
    }

    public void setDetailsItemList(List<SenderDetailsItem> detailsItemList) {
        this.detailsItemList = detailsItemList;
        notifyDataSetChanged();
    }

    public class SenderDetailsItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvPhoneNumber)
        TextView tvPhoneNumber;
        @BindView(R.id.radio)
        RadioButton radioButton;

        private RecyclerViewItemClickListener recyclerViewItemClickListener;

        private SenderDetailsItemHolder(@NonNull View itemView, RecyclerViewItemClickListener recyclerViewItemClickListener) {
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

        void onBindViewHolder(SenderDetailsItem senderDetailsItem) {
            tvName.setText(senderDetailsItem.getName());
            tvPhoneNumber.setText(senderDetailsItem.getPhoneNumber());
            radioButton.setChecked(senderDetailsItem.isSelected());
        }
    }
}