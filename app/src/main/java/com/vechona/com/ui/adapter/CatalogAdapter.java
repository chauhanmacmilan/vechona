package com.vechona.com.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vechona.app.R;
import com.vechona.com.ui.interfaces.OnItemClickListener;
import com.vechona.com.ui.model.Product;
import com.vechona.com.ui.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.MyViewHolder> {
    private final OnItemClickListener onItemClickListener;
    private List<Product> products;

    public CatalogAdapter(OnItemClickListener onItemClickListener) {
        products = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public void setCatalog(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.catalog_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.onBindViewHolder(products.get(i));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ivCatalog)
        AppCompatImageView ivCatalog;

        @BindView(R.id.tvCatalogName)
        TextView tvCatalogName;

        @BindView(R.id.tvCatalogPrice)
        TextView tvCatalogPrice;

        @BindView(R.id.llshareOnwhatsapp)
        LinearLayout llshareOnWhatsapp;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        void onBindViewHolder(Product product) {
            Glide.with(itemView.getContext()).load(product.getProductsImageUrls().get(0).getImageUrl()).into(ivCatalog);
            tvCatalogName.setText(StringUtils.camelCase(product.getProductName()));
            tvCatalogPrice.setText(itemView.getContext().getString(R.string.priceFormat, product.getPrice()));
            llshareOnWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClickShareNow(v, getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (RecyclerView.NO_POSITION != getAdapterPosition())
                onItemClickListener.onClickAtPosition(v, getAdapterPosition());
        }
    }
}
