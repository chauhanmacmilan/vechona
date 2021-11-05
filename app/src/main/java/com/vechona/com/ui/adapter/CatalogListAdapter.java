package com.vechona.com.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.ui.interfaces.OnItemClickListener;
import com.vechona.com.ui.model.Catalog;
import com.vechona.com.ui.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CatalogListAdapter extends RecyclerView.Adapter<CatalogListAdapter.MyViewHolder> implements Filterable {

    private OnItemClickListener onItemClickListener;
    private List<Catalog> catalogList;
    private List<Catalog> catalogfilter;

    public CatalogListAdapter(OnItemClickListener onItemClickListener) {
        catalogList = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CatalogListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.catalog_list_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogListAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.onBindViewHolder(catalogList.get(i));
    }

    @Override
    public int getItemCount() {
        return catalogList != null ? catalogList.size() : 0;
    }

    public void setCatalogList(List<Catalog> catalogList) {
        this.catalogList = catalogList;
        this.catalogfilter = catalogList;
        notifyDataSetChanged();
    }

    public List<Catalog> getCatalogItems() {
        return catalogList;
    }

    public void deleteItem(int index) {
        catalogList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    catalogList = catalogfilter;
                } else {
                    List<Catalog> filteredList = new ArrayList<>();
                    for (Catalog catalog : catalogfilter) {

                        if (catalog.getCatalogName().toLowerCase().contains(charString.toLowerCase()) || catalog.getCategoryName().contains(charSequence)) {
                            filteredList.add(catalog);
                        }
                    }

                    catalogList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = catalogList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                catalogList = (ArrayList<Catalog>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ivCataloglist)
        ImageView ivCataloglist;

        @BindView(R.id.ivCataloglist2)
        ImageView ivCataloglist2;

        @BindView(R.id.ivCataloglist3)
        ImageView ivCataloglist3;

        @BindView(R.id.tvCataloglistName)
        TextView tvCataloglistName;

        @BindView(R.id.llCatalogRating)
        LinearLayout llCatalogRating;

        @BindView(R.id.tvCatalogRating)
        TextView tvCatalogRating;

        @BindView(R.id.imageCount)
        TextView tvImageCount;

        @BindView(R.id.tvCataloglistPrice)
        TextView tvCataloglistprice;

        @BindView(R.id.faverite)
        AppCompatImageView faverite;

        @BindView(R.id.btn_whatsapp)
        LinearLayout llbtnWhatsapp;

        @BindView(R.id.rlPrebook)
        RelativeLayout rlPrebook;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void onBindViewHolder(final Catalog catalog) {

            if (catalog.getProducts() != null && catalog.getProducts().size() != 0) {
                //for (int i = 0; i < catalog.getProducts().size(); i++) {
                if (catalog.getProducts().size() >= 3) {
                    Glide.with(itemView.getContext()).load(catalog.getProducts().get(0).getProductsImageUrls().get(0).getImageUrl()).apply(new RequestOptions().error(R.drawable.ic_list)).into(ivCataloglist);
                    Glide.with(itemView.getContext()).load(catalog.getProducts().get(1).getProductsImageUrls().get(0).getImageUrl()).apply(new RequestOptions().error(R.drawable.ic_list)).into(ivCataloglist2);
                    Glide.with(itemView.getContext()).load(catalog.getProducts().get(2).getProductsImageUrls().get(0).getImageUrl()).apply(new RequestOptions().error(R.drawable.ic_list)).into(ivCataloglist3);
                } else {
                    if (catalog.getProducts().size() == 1) {
                        Glide.with(itemView.getContext()).load(catalog.getProducts().get(0).getProductsImageUrls().get(0).getImageUrl()).apply(new RequestOptions().error(R.drawable.ic_list)).into(ivCataloglist);
                        ivCataloglist2.setImageResource(R.drawable.ic_list);
                    } else {
                        Glide.with(itemView.getContext()).load(catalog.getProducts().get(0).getProductsImageUrls().get(0).getImageUrl()).apply(new RequestOptions().error(R.drawable.ic_list)).into(ivCataloglist);
                        Glide.with(itemView.getContext()).load(catalog.getProducts().get(1).getProductsImageUrls().get(0).getImageUrl()).apply(new RequestOptions().error(R.drawable.ic_list)).into(ivCataloglist2);
                    }
                    ivCataloglist3.setImageResource(R.drawable.ic_list);
                }
                //}
                if (catalog.getProducts().size() > 3)
                    tvImageCount.setText(itemView.getContext().getString(R.string.image_count, catalog.getProducts().size() - 3));
                tvCataloglistprice.setText(itemView.getContext().getString(R.string.priceFormat, catalog.getCatalogBasePrice()));
            }
            if (catalog.is_prepaid())
                rlPrebook.setVisibility(View.VISIBLE);
            else
                rlPrebook.setVisibility(View.GONE);
            tvCataloglistName.setText(StringUtils.camelCase(catalog.getCatalogName()));
            if (!catalog.getAverageRating().equals("0")) {
                llCatalogRating.setVisibility(View.VISIBLE);
                tvCatalogRating.setText(String.format("%.1f", Float.parseFloat(catalog.getAverageRating())));
            } else llCatalogRating.setVisibility(View.GONE);


            if (PreferenceDataHelper.getInstance(itemView.getContext()).isWishOrShareList(catalog, true))
                faverite.setImageResource(R.drawable.ic_favorite);
            else
                faverite.setImageResource(R.drawable.ic_favorite_border_black_24dp);

            faverite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!PreferenceDataHelper.getInstance(itemView.getContext()).isWishOrShareList(catalog, true))
                        faverite.setImageResource(R.drawable.ic_favorite);
                    else
                        faverite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    onItemClickListener.onClickWishList(v, catalog.getCatalogId());
                }
            });
            llbtnWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClickShareNow(v, catalog.getCatalogId());
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (RecyclerView.NO_POSITION != getAdapterPosition())
                onItemClickListener.onClickAtPosition(v, catalogList.get(getAdapterPosition()).getCatalogId());
        }
    }
}
