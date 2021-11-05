package com.vechona.com.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.vechona.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SortBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_CHECKED_TEXT = "checked_TEXT";
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    private OnCheckChanged onCheckChanged;

    public static SortBottomSheet newInstance(String checkedText) {
        Bundle args = new Bundle();
        args.putString(ARG_CHECKED_TEXT, checkedText);

        SortBottomSheet dialog = new SortBottomSheet();
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sort_bottom_sheet, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.ivClose)
    public void onClickClose() {
        dismiss();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().getString(ARG_CHECKED_TEXT) != null) {
            if (getArguments().getString(ARG_CHECKED_TEXT).equals(getString(R.string.most_popular)))
                radioGroup.check(R.id.radioMostPopular);
            else if (getArguments().getString(ARG_CHECKED_TEXT).equals(getString(R.string.price_high_to_low)))
                radioGroup.check(R.id.radioPriceHL);
            else if (getArguments().getString(ARG_CHECKED_TEXT).equals(getString(R.string.price_low_to_high)))
                radioGroup.check(R.id.radioPriceLH);
            else if (getArguments().getString(ARG_CHECKED_TEXT).equals(getString(R.string.rating)))
                radioGroup.check(R.id.radioRatting);
            else
                radioGroup.check(R.id.radioNewArrivals);
        } else radioGroup.check(R.id.radioNewArrivals);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) view.findViewById(checkedId);
                String sortItemLabel = rb.getText().toString();
                onCheckChanged.onChecked(sortItemLabel);
                dismiss();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onCheckChanged = (OnCheckChanged) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must override onChecked...");
        }
    }

    public interface OnCheckChanged {
        void onChecked(String text);
    }

}
