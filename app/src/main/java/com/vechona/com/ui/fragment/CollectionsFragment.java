package com.vechona.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.CollectionResponse;
import com.vechona.com.data.remote.apiResponse.CollectionResponseData;
import com.vechona.com.ui.activity.CartActivity;
import com.vechona.com.ui.activity.CatalogListActivity;
import com.vechona.com.ui.adapter.CollectionAdapter;
import com.vechona.com.ui.interfaces.ItemClickListener;
import com.vechona.com.ui.model.Collection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionsFragment extends Fragment {

    @BindView(R.id.titleTV)
    TextView toolbar_title;

    @BindView(R.id.rvCollections)
    RecyclerView rvCollections;

    @BindView(R.id.ivMenuSearch)
    AppCompatImageView ivMenuSearch;
    @BindView(R.id.rlCart)
    RelativeLayout rlCart;
    @BindView(R.id.tvCartBadge)
    TextView tvCartBadge;

    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private CollectionAdapter collectionsAdapter;
    private List<Collection> collections;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collections, container, false);
        ButterKnife.bind(this, view);
        toolbar_title.setText(R.string.menu_collections);
        ivMenuSearch.setVisibility(View.VISIBLE);
        rlCart.setVisibility(View.VISIBLE);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCollections.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        collectionsAdapter = new CollectionAdapter(new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                CatalogListActivity
                        .start(getContext(),
                                collections.get(position).getCollectionId(),
                                collections.get(position).getCollectionName());
            }
        });
        rvCollections.setAdapter(collectionsAdapter);

        GridLayoutManager gd = new GridLayoutManager(getContext(), 2);
        gd.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return collectionsAdapter.getItemViewType(position) == 0 ? 2 : 1;
            }
        });

        rvCollections.setLayoutManager(gd);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getCollection();
    }

    @Override
    public void onResume() {
        super.onResume();
        setCartBadgeCount();
    }

    @OnClick(R.id.rlCart)
    public void onCartClick() {
        CartActivity.start(getContext());
    }

    @OnClick(R.id.ivMenuSearch)
    public void onClickSearch() {
        CatalogListActivity.start(getContext(), true);
    }

    private void setCartBadgeCount() {
        int count = PreferenceDataHelper.getInstance(getContext()).getCartItemCount();
        if (count == 0) tvCartBadge.setVisibility(View.GONE);
        else {
            tvCartBadge.setVisibility(View.VISIBLE);
            tvCartBadge.setText(String.valueOf(count));
        }
    }

    private void getCollection() {
        showLoading();
        ApiService apiService =
                RemoteServiceHelper.createService(ApiService.class,
                        RemoteServiceHelper.getRetrofitInstance(getContext()));
        apiService.getCollections().enqueue(new Callback<CollectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<CollectionResponse> call, @NonNull Response<CollectionResponse> response) {
                CollectionResponseData collectionResponseData = response.body().getCollectionResponseData();
                if (response.isSuccessful() && collectionResponseData != null) {
                    collections = collectionResponseData.getCollectionList();
                    collectionsAdapter.setItems(collections);
                    hideLoading();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CollectionResponse> call, @NonNull Throwable t) {
                hideLoading();
                Toast.makeText(getContext(), "connection failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showLoading() {
        progressWheel.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressWheel.setVisibility(View.GONE);
    }
}
