package com.vechona.com.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vechona.app.R;
import com.vechona.com.ui.custom.CircleImageView;
import com.vechona.com.ui.interfaces.ItemClickListener;
import com.vechona.com.ui.model.Category;
import com.vechona.com.ui.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {

    private final ItemClickListener itemClickListener;
    private List<Category> topCatagoryList;

    public CategoriesAdapter(ItemClickListener itemClickListener) {
        topCatagoryList = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CategoriesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_categories, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.MyViewHolder holder, int position) {
        holder.onBindViewHolder(topCatagoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return topCatagoryList.size();
    }

    public void setItems(List<Category> topCategories) {
        this.topCatagoryList = topCategories;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvCategotyName)
        TextView name;

        @BindView(R.id.ivCategory)
        CircleImageView iv;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void onBindViewHolder(Category category) {
            if (category.getImageUrl() != null)
                Glide.with(itemView.getContext()).load(category.getImageUrl()).into(iv);
            else
                Glide.with(itemView.getContext()).load(R.drawable.ic_list).into(iv);
            name.setText(StringUtils.camelCase(category.getCategoryName()));
        }

        @Override
        public void onClick(View v) {
            if (RecyclerView.NO_POSITION != getAdapterPosition())
                itemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
