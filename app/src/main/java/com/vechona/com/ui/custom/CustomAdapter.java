package com.vechona.com.ui.custom;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vechona.app.R;
import com.vechona.com.ui.activity.CatalogListActivity;
import com.vechona.com.ui.model.HomeSlider;

import java.util.List;

public class CustomAdapter extends PagerAdapter {
    private List<HomeSlider> sliderList;

    public CustomAdapter(List<HomeSlider> sliderList){
        this.sliderList = sliderList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup viewGroup, final int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View viewItem = inflater.inflate(R.layout.image_item, viewGroup, false);
        ImageView imageView = viewItem.findViewById(R.id.imageView);
        if (sliderList.get(position).getSliderImageUrl() != null)
            Glide.with(viewGroup.getContext()).load(sliderList.get(position).getSliderImageUrl()).into(imageView);
        else
            Glide.with(viewGroup.getContext()).load(R.drawable.ic_list).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatalogListActivity.start(v.getContext(),
                        sliderList.get(position).getSliderCollectionID(),
                        sliderList.get(position).getCollectionName());
            }
        });
        viewGroup.addView(viewItem);
        return viewItem;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return sliderList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        // TODO Auto-generated method stub
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // TODO Auto-generated method stub
        container.removeView((View) object);
    }

    public void setItems(List<HomeSlider> sliderList) {
        this.sliderList = sliderList;
        notifyDataSetChanged();
    }
}

