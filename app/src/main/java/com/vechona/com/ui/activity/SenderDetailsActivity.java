package com.vechona.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.vechona.app.R;
import com.vechona.com.data.local.PreferenceDataHelper;
import com.vechona.com.ui.adapter.SenderDetailsItemAdapter;
import com.vechona.com.ui.fragment.AddSenderDialog;
import com.vechona.com.ui.interfaces.RecyclerViewItemClickListener;
import com.vechona.com.ui.model.SenderDetailsItem;
import com.vechona.com.ui.utils.BundleParams;
import com.vechona.com.ui.utils.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SenderDetailsActivity extends AppCompatActivity implements AddSenderDialog.AddSenderDialogListener {

    @BindView(R.id.rvSenderDetails)
    RecyclerView rvSenderDetails;

    @BindView(R.id.btnProceed)
    Button btnProceed;

    private List<SenderDetailsItem> detailsItemList;
    private SenderDetailsItemAdapter detailsItemAdapter;
    private PreferenceDataHelper preferenceDataHelper;

    public static void start(Context context, String paymentMethod, String price) {
        Intent intent = new Intent(context, SenderDetailsActivity.class);
        intent.putExtra(BundleParams.PAYMENT_METHOD, paymentMethod);
        intent.putExtra(BundleParams.PRICE, price);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender_details);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initView() {
        detailsItemAdapter = new SenderDetailsItemAdapter(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showAddSenderDialog(true, position);
            }

            @Override
            public void onRadioClick(int adapterPosition, int position) {
                detailsItemList.get(adapterPosition).setSelected(true);
                if (position != adapterPosition)
                    detailsItemList.get(position).setSelected(false);
                preferenceDataHelper.saveSenderDetailsList(detailsItemList);
                detailsItemAdapter.notifyItemChanged(adapterPosition);
                detailsItemAdapter.notifyItemChanged(position);
            }
        });

        rvSenderDetails.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSenderDetails.setAdapter(detailsItemAdapter);
    }

    private void initData() {
        preferenceDataHelper = PreferenceDataHelper.getInstance(SenderDetailsActivity.this);
        prepareList();
    }

    private void prepareList() {
        detailsItemList = preferenceDataHelper.getSenderDetailsList();
        if (detailsItemList != null && detailsItemList.size() > 0)
            detailsItemAdapter.setDetailsItemList(detailsItemList);
    }

    @OnClick(R.id.ivCartToolbarBack)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.llAddNew)
    public void onAddNewClick() {
        showAddSenderDialog(false, -1);
    }

    @OnClick(R.id.btnProceed)
    public void onProceedButtonClick() {
        String paymentMethod = getIntent().getStringExtra(BundleParams.PAYMENT_METHOD);
        String price = getIntent().getStringExtra(BundleParams.PRICE);
        if (detailsItemList != null && preferenceDataHelper.getSelectedSenderDetail() != null)
            OrderConfirmationActivity.start(SenderDetailsActivity.this, paymentMethod, price);
        else {
            if (detailsItemList != null && detailsItemList.size() > 0)
                ViewUtils.showToast(this, getString(R.string.alert_select_item));
            else
                ViewUtils.showToast(this, getString(R.string.alert_add_sender_details));
        }
    }

    private void showAddSenderDialog(boolean isEdit, int position) {
        String tag = "AddSenderDetailDialog";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null)
            return;
        AddSenderDialog dialog = AddSenderDialog.newInstance(isEdit, position, position != -1 ? detailsItemList.get(position) : null);
        dialog.show(fragmentTransaction, tag);
    }

    @Override
    public void onAddSenderDialogSubmitClick(boolean isEdit, int position, SenderDetailsItem senderDetailsItem) {
        if (isEdit && position != -1 && senderDetailsItem != null) {
            detailsItemList.remove(position);
            detailsItemList.add(position, senderDetailsItem);
            preferenceDataHelper.saveSenderDetailsList(detailsItemList);
        } else preferenceDataHelper.addSenderDetail(senderDetailsItem);

        prepareList();
    }
}