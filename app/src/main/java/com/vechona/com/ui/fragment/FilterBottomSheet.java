package com.vechona.com.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vechona.app.R;
import com.vechona.com.ui.adapter.FilterAdapter;
import com.vechona.com.ui.adapter.FilterButtonAdapter;
import com.vechona.com.ui.interfaces.FilterItemClickListener;
import com.vechona.com.ui.interfaces.ItemClickListener;
import com.vechona.com.ui.model.Filter;
import com.vechona.com.ui.model.FilterButtonModel;
import com.vechona.com.ui.model.FilterItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_FILTERS = "arg_filters";
    private static final int FILTER_TYPE_CATEGORY = 0;
    private static final int FILTER_TYPE_PRICE = 1;
    private static final int FILTER_TYPE_RATING = 2;
    //private static final int FILTER_TYPE_DISCOUNT = 3;

    @BindView(R.id.rvButtons)
    RecyclerView rvButtons;

    @BindView(R.id.rvItems)
    RecyclerView rvItems;

    private List<FilterItem> categoryFilterlist = new ArrayList<>();
    private List<FilterItem> priceFilterlist = new ArrayList<>();
    private List<FilterItem> ratingFilterlist = new ArrayList<>();
    //private List<FilterItem> discountFilterlist = new ArrayList<>();

    private FilterAdapter filterAdapter;
    private FilterButtonAdapter filterButtonAdapter;
    private List<FilterButtonModel> filterButtons = new ArrayList<>();
    private List<Filter> filterList = new ArrayList<>();
    private Filter categoryFilter;
    private Filter priceFilter;
    private Filter ratingFilter;
    //private Filter discountFilter;
    private OnApplyChanged onApplyChanged;

    public static FilterBottomSheet newInstance(List<Filter> filterList) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_FILTERS, (ArrayList<? extends Parcelable>) filterList);

        FilterBottomSheet dialog = new FilterBottomSheet();
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_bottom_sheet, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filterList = getArguments().getParcelableArrayList(ARG_FILTERS);
        for (Filter filter : filterList) {
            if (filter.getItemType().equals(getString(R.string.title_category))) {
                categoryFilter = filter;
                categoryFilterlist = filter.getFilterItems();
            } else if (filter.getItemType().equals(getString(R.string.title_price))) {
                priceFilter = filter;
                priceFilterlist = filter.getFilterItems();
            } else if (filter.getItemType().equals(getString(R.string.title_rating))) {
                ratingFilter = filter;
                ratingFilterlist = filter.getFilterItems();
            } /*else {
                discountFilter = filter;
                discountFilterlist = filter.getFilterItems();
            }*/
        }

        filterButtonAdapter = new FilterButtonAdapter(new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (filterButtons.get(position).getBtnTitle().equalsIgnoreCase(getString(R.string.title_category)))
                    setCategories();
                else if (filterButtons.get(position).getBtnTitle().equalsIgnoreCase(getString(R.string.title_price)))
                    setPrice();
                else if (filterButtons.get(position).getBtnTitle().equalsIgnoreCase(getString(R.string.title_rating)))
                    setRating();
               /* else
                    setDiscount();*/
            }
        });
        rvButtons.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        rvButtons.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvButtons.setAdapter(filterButtonAdapter);

        initButtons();

        filterAdapter = new FilterAdapter(new FilterItemClickListener() {
            @Override
            public void onChechBoxClick(int position, int type) {
                if (type == FILTER_TYPE_CATEGORY) {
                    if (categoryFilterlist.get(position).isSelected())
                        categoryFilterlist.get(position).setSelected(false);
                    else
                        categoryFilterlist.get(position).setSelected(true);
                } else if (type == FILTER_TYPE_PRICE) {
                    if (priceFilterlist.get(position).isSelected()) {
                        priceFilterlist.get(position).setSelected(false);
                    } else {
                        priceFilterlist.get(position).setSelected(true);
                    }
                }
                filterAdapter.notifyItemChanged(position);
            }

            @Override
            public void onRadioClick(int adapterPosition, int position, int type) {
                if (type == FILTER_TYPE_RATING) {
                    ratingFilterlist.get(adapterPosition).setSelected(true);
                    if (position != adapterPosition)
                        ratingFilterlist.get(position).setSelected(false);
                } /*else if (type == FILTER_TYPE_DISCOUNT) {
                    discountFilterlist.get(adapterPosition).setSelected(true);
                    if (position != adapterPosition)
                        discountFilterlist.get(position).setSelected(false);
                }*/
                filterAdapter.notifyItemChanged(adapterPosition);
                filterAdapter.notifyItemChanged(position);
            }
        });
        rvItems.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvItems.setAdapter(filterAdapter);
        setCategories();

    }

    private void initButtons() {
        String[] btnTitles = new String[]{getString(R.string.title_category),
                getString(R.string.title_price),
                getString(R.string.title_rating)/*,
                getString(R.string.discount)*/};
        FilterButtonModel filterButton;
        for (String title : btnTitles) {
            filterButton = new FilterButtonModel(title);
            filterButtons.add(filterButton);
        }
        filterButtonAdapter.setDate(filterButtons);
    }

    @OnClick(R.id.ivClose)
    public void onClickClose() {
        dismiss();
    }

    private void setCategories() {
        filterAdapter.setData(categoryFilterlist, categoryFilter.isMultiSelected(), FILTER_TYPE_CATEGORY);
    }

    private void setPrice() {
        filterAdapter.setData(priceFilterlist, priceFilter.isMultiSelected(), FILTER_TYPE_PRICE);
    }

    private void setRating() {
        filterAdapter.setData(ratingFilterlist, ratingFilter.isMultiSelected(), FILTER_TYPE_RATING);
    }

    /*private void setDiscount() {
        filterAdapter.setData(discountFilterlist, discountFilter.isMultiSelected(), FILTER_TYPE_DISCOUNT);
    }*/

    @OnClick(R.id.btnApplyChanges)
    public void onClickApplyChanges() {
        filterList.clear();

        filterList.add(categoryFilter);

        filterList.add(priceFilter);

        filterList.add(ratingFilter);

//        filterList.add(discountfilter);

        onApplyChanged.onClickApplyChangedListner(filterList);
        dismiss();
    }

    @OnClick(R.id.btnReset)
    public void onClickReset() {
        resetFilter(categoryFilter);
        resetFilter(priceFilter);
        resetFilter(ratingFilter);
        //resetFilter(discountFilter);
        setCategories();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onApplyChanged = (OnApplyChanged) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must override onChecked...");
        }
    }

    private void resetFilter(Filter filter) {
        for (FilterItem filterItem : filter.getFilterItems()) {
            filterItem.setSelected(false);
        }
    }

    public interface OnApplyChanged {
        void onClickApplyChangedListner(List<Filter> filterList);
    }
}
