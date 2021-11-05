package com.vechona.com.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vechona.app.R;
import com.vechona.com.ui.model.ProductsImageUrl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemZoomImageAdapter extends RecyclerView.Adapter<ItemZoomImageAdapter.MyViewHolder> {

    private List<ProductsImageUrl> productImageUrls;

    public ItemZoomImageAdapter(List<ProductsImageUrl> productsImageUrls) {
        productImageUrls = new ArrayList<>();
        this.productImageUrls = productsImageUrls;
    }

    @NonNull
    @Override
    public ItemZoomImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_zoom, viewGroup, false);
        return new ItemZoomImageAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.onBindViewHolder(productImageUrls.get(i));

    }

    @Override
    public int getItemCount() {
        return productImageUrls.size();
    }

    public void setItems(List<ProductsImageUrl> productsImageUrls) {
        this.productImageUrls = productsImageUrls;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.zoomImage)
        ImageView zoomImage;

        public MyViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void onBindViewHolder(ProductsImageUrl productsImageUrl) {
            Glide.with(itemView.getContext()).load(productsImageUrl.getImageUrl()).into(zoomImage);
        }
    }

}

