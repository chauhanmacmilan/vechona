package com.vechona.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.data.remote.ApiService;
import com.vechona.com.data.remote.RemoteServiceHelper;
import com.vechona.com.data.remote.apiResponse.UserOrderListResponse;
import com.vechona.com.ui.activity.CartActivity;
import com.vechona.com.ui.activity.CatalogListActivity;
import com.vechona.com.ui.activity.OrderDetailActivity;
import com.vechona.com.ui.adapter.OrderItemAdapter;
import com.vechona.com.ui.interfaces.ItemClickListener;
import com.vechona.com.ui.model.UserOrderItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {

    @BindView(R.id.titleTV)
    TextView toolbar_title;

    @BindView(R.id.ivMenuSearch)
    AppCompatImageView ivMenuSearch;

    @BindView(R.id.rlCart)
    RelativeLayout rlCart;

    @BindView(R.id.tvCartBadge)
    TextView tvCartBadge;

    @BindView(R.id.rvOrder)
    RecyclerView rvOrder;

    @BindView(R.id.progress_wheel)
    ProgressWheel progressWheel;

    private OrderItemAdapter orderItemAdapter;
    private List<UserOrderItem> userOrderItemList;
    private PreferenceDataHelper preferenceDataHelper;
    private ApiService apiService;

    public static OrderFragment newInstance() {
        Bundle args = new Bundle();
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        toolbar_title.setText(R.string.menu_order);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        intiData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && apiService != null && preferenceDataHelper != null)
            prepareOrderList();
    }

    private void initView() {
        ivMenuSearch.setVisibility(View.VISIBLE);
        rlCart.setVisibility(View.VISIBLE);
        orderItemAdapter = new OrderItemAdapter(new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                OrderDetailActivity.start(getContext(), userOrderItemList.get(position));
            }
        });
        rvOrder.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        rvOrder.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvOrder.setAdapter(orderItemAdapter);
    }

    private void intiData() {
        apiService = RemoteServiceHelper.createService(ApiService.class,
                RemoteServiceHelper.getRetrofitInstance(getContext()));
        preferenceDataHelper = PreferenceDataHelper.getInstance(getContext());
        prepareOrderList();
    }

    private void prepareOrderList() {
        showLoading();
        apiService.getUserOrderList(preferenceDataHelper.getUser().getUserID()).enqueue(new Callback<UserOrderListResponse>() {
            @Override
            public void onResponse(Call<UserOrderListResponse> call, Response<UserOrderListResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().getStatus().equals("success")) {
                    hideLoading();
                    userOrderItemList = response.body().getUserOrderListData().getUserOrderItems();
                    if (userOrderItemList != null && userOrderItemList.size() > 0)
                        setUserOrderList();
                }
            }

            @Override
            public void onFailure(Call<UserOrderListResponse> call, Throwable t) {
                hideLoading();
            }
        });
    }

    private void setUserOrderList() {
        Collections.sort(userOrderItemList, new Comparator<UserOrderItem>() {
            @Override
            public int compare(UserOrderItem o1, UserOrderItem o2) {
                return o2.getOrderCreationDate().compareTo(o1.getOrderCreationDate());
            }
        });
        orderItemAdapter.setUserOrderItemList(userOrderItemList);
    }

    @OnClick(R.id.rlCart)
    public void onClickCart() {
        CartActivity.start(getContext());
    }

    @OnClick(R.id.ivMenuSearch)
    public void onClickSearch() {
        CatalogListActivity.start(getContext(), true);
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.collection_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    @Override
    public void onResume() {
        super.onResume();
        setCartBadgeCount();
    }

    private void setCartBadgeCount() {
        int count = PreferenceDataHelper.getInstance(getContext()).getCartItemCount();
        if (count == 0) tvCartBadge.setVisibility(View.GONE);
        else {
            tvCartBadge.setVisibility(View.VISIBLE);
            tvCartBadge.setText(String.valueOf(count));
        }
    }

    public void showLoading() {
        progressWheel.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressWheel.setVisibility(View.GONE);
    }

}
