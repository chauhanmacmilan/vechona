package com.vechona.com.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vechona.app.R;
import com.vechona.com.ui.interfaces.ItemClickListener;
import com.vechona.com.ui.model.FilterButtonModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterButtonAdapter extends RecyclerView.Adapter<FilterButtonAdapter.MyViewHolder> {
    private final ItemClickListener itemClickListener;
    private List<FilterButtonModel> filterButtons;
    private int selectedPosition = 0;

    public FilterButtonAdapter(ItemClickListener itemClickListener) {
        filterButtons = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public FilterButtonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_filter_buttons, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterButtonAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.bindViewHolder(filterButtons.get(i));
    }

    @Override
    public int getItemCount() {
        return filterButtons.size();
    }

    public void setDate(List<FilterButtonModel> filterButtons) {
        this.filterButtons = filterButtons;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvText)
        TextView tvText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bindViewHolder(FilterButtonModel filterButtonModel) {
            tvText.setText(filterButtonModel.getBtnTitle());
            if (selectedPosition == getAdapterPosition()) {
                tvText.setBackgroundColor(itemView.getResources().getColor(R.color.white));
                tvText.setTextColor(itemView.getResources().getColor(R.color.black));
            } else {
                tvText.setBackgroundColor(itemView.getResources().getColor(R.color.trans_grey_60));
                tvText.setTextColor(itemView.getResources().getColor(R.color.grey_60));
            }
        }

        @Override
        public void onClick(View v) {
            if (RecyclerView.NO_POSITION != getAdapterPosition()) {
                itemClickListener.onItemClick(getAdapterPosition());
                selectedPosition = getAdapterPosition();
                notifyDataSetChanged();
            }
        }
    }
}
