package com.vechona.com.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vechona.app.R;
import com.vechona.com.ui.interfaces.ItemClickListener;
import com.vechona.com.ui.model.Collection;
import com.vechona.com.ui.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {

    private final ItemClickListener itemClickListener;
    private List<Collection> collections;

    public CollectionAdapter(ItemClickListener itemClickListener) {
        collections = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CollectionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.collection_list_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.onBindViewHolder(collections.get(i));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public void setItems(List<Collection> collections) {
        this.collections = collections;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvlist)
        TextView title;

        @BindView(R.id.ivlist)
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void onBindViewHolder(Collection collection) {
            if (collection.getCollectionImageUrl() != null)
                Glide.with(itemView.getContext()).load(collection.getCollectionImageUrl()).into(image);
            else
                Glide.with(itemView.getContext()).load(R.drawable.ic_list).into(image);

            title.setText(StringUtils.camelCase(collection.getCollectionName()));
        }

        @Override
        public void onClick(View v) {
            if (RecyclerView.NO_POSITION != getAdapterPosition()) {
                itemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
}
