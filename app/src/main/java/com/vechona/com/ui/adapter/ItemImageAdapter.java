package com.vechona.com.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vechona.app.R;
import com.vechona.com.ui.interfaces.OnItemClickListener;
import com.vechona.com.ui.model.ProductsImageUrl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemImageAdapter extends RecyclerView.Adapter<ItemImageAdapter.MyViewHolder> {
    private List<ProductsImageUrl> productImageUrls;
    private OnItemClickListener itemClickListner;

    public ItemImageAdapter(OnItemClickListener itemClickListner) {
        this.itemClickListner = itemClickListner;
        productImageUrls = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item, viewGroup, false);
        return new MyViewHolder(view, itemClickListner);
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

        @BindView(R.id.imageView)
        ImageView imageView;

        MyViewHolder(View view, final OnItemClickListener itemClickListner) {
            super(view);
            ButterKnife.bind(this, view);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListner.onClickAtPosition(view, getAdapterPosition());
                }
            });
        }

        public void onBindViewHolder(ProductsImageUrl productsImageUrl) {
            Glide.with(itemView.getContext()).load(productsImageUrl.getImageUrl()).into(imageView);
        }
    }
}
