package com.vechona.com.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vechona.app.R;
import com.vechona.com.ui.interfaces.FilterItemClickListener;
import com.vechona.com.ui.model.FilterItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_CHECKBOX = 0;
    private static final int TYPE_RADIO = 1;
    private final FilterItemClickListener filterItemClickListener;
    private List<FilterItem> filterData;
    private int selectedRadioPostion;
    private int type;
    private boolean isMultiselected;

    public FilterAdapter(FilterItemClickListener filterItemClickListener) {
        filterData = new ArrayList<>();
        this.filterItemClickListener = filterItemClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case TYPE_CHECKBOX:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_filter_checkbox, viewGroup, false);
                return new VHCheckBox(view);
            case TYPE_RADIO:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_filter_radiobutton, viewGroup, false);
                return new VHRadio(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (getItemViewType(i) == TYPE_CHECKBOX)
            ((VHCheckBox) holder).setCheckDetails(filterData.get(i));
        else {
            ((VHRadio) holder).setRadioDetails(filterData.get(i));
            if (filterData.get(i).isSelected()) selectedRadioPostion = i;
        }
    }

    @Override
    public int getItemCount() {
        return filterData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isMultiselected)
            return TYPE_CHECKBOX;
        else
            return TYPE_RADIO;
    }

    public void setData(List<FilterItem> filters, boolean isMultiselected, int type) {
        this.filterData = filters;
        this.type = type;
        this.isMultiselected = isMultiselected;
        notifyDataSetChanged();
    }

    public void clear() {
        int size = filterData.size();
        filterData.clear();
        notifyItemRangeRemoved(0, size);
    }

    class VHCheckBox extends RecyclerView.ViewHolder {

        @BindView(R.id.checkbox)
        CheckBox checkBox;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        /*@BindView(R.id.tvNoOfItems)
        TextView tvNoOfItems;*/

        VHCheckBox(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setCheckDetails(FilterItem filterData) {
            tvTitle.setText(filterData.getName());
            //tvNoOfItems.setText(String.valueOf(filterData.getItemCount()));
            if (filterData.isSelected()) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

        }

        @OnClick(R.id.checkbox)
        public void onClickCheckBox() {
            int adapterPosition = getAdapterPosition();
            filterItemClickListener.onChechBoxClick(adapterPosition, type);
            if (filterData.get(adapterPosition).isSelected()) {
                checkBox.setChecked(false);
            } else {
                checkBox.setChecked(true);
            }
        }
    }

    class VHRadio extends RecyclerView.ViewHolder {

        @BindView(R.id.radioButton)
        RadioButton radioButton;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
       /* @BindView(R.id.tvNoOfItems)
        TextView tvNoOfItems;*/

        VHRadio(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setRadioDetails(FilterItem filterData) {
            tvTitle.setText(filterData.getName());
            //tvNoOfItems.setText(String.valueOf(filterData.getItemCount()));
            radioButton.setChecked(filterData.isSelected());
        }

        @OnClick(R.id.radioButton)
        public void onClickRadio() {
            filterItemClickListener.onRadioClick(getAdapterPosition(), selectedRadioPostion, type);
            selectedRadioPostion = getAdapterPosition();
        }
    }
}
