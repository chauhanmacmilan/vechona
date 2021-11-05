package com.vechona.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vechona.app.R;
import com.vechona.com.ui.adapter.ItemZoomImageAdapter;
import com.vechona.com.ui.model.Product;
import com.vechona.com.ui.model.ProductsImageUrl;
import com.vechona.com.ui.utils.BundleParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageZoomActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rvZoom)
    RecyclerView rvZoom;

    private Product product;
    private int position;
    private ItemZoomImageAdapter itemZoomImageAdapter;
    private List<ProductsImageUrl> productsImageUrls = new ArrayList<>();

    public static void start(Context context, Product product, int Position) {
        Intent newIntent = new Intent(context, ImageZoomActivity.class);
        newIntent.putExtra(BundleParams.PRODUCT, product);
        newIntent.putExtra(BundleParams.POSITION, Position);
        context.startActivity(newIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);
        ButterKnife.bind(this);
        initView();

        product = getIntent().getExtras().getParcelable(BundleParams.PRODUCT);
        getSupportActionBar().setTitle(product.getProductName());
        position = getIntent().getExtras().getInt(BundleParams.POSITION);
        productsImageUrls = product.getProductsImageUrls();

        itemZoomImageAdapter = new ItemZoomImageAdapter(productsImageUrls);
        rvZoom.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        rvZoom.setAdapter(itemZoomImageAdapter);
        rvZoom.scrollToPosition(position);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvZoom);

    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}