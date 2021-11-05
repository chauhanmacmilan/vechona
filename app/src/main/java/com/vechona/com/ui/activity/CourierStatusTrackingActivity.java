package com.vechona.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vechona.app.R;
import com.vechona.com.ui.adapter.TrackingdetailsAdapter;
import com.vechona.com.ui.model.Tracking;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourierStatusTrackingActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rvTrackingdetail)
    RecyclerView rvTrackingdetail;

    private TrackingdetailsAdapter trackingdetailsAdapter;
    private List<Tracking> trackings = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, CourierStatusTrackingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_status_tracking);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.tracking);
        initView();

        trackingdetailsAdapter = new TrackingdetailsAdapter(trackings, CourierStatusTrackingActivity.this);
        rvTrackingdetail.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvTrackingdetail.setItemAnimator(new DefaultItemAnimator());
        rvTrackingdetail.setAdapter(trackingdetailsAdapter);

        prepareTrackingData();
    }

    private void prepareTrackingData() {

        Tracking tracking = new Tracking("21 Dec,09:10 PM", "To Cancel something means to stop doing or to stop planning to do something. The word cancellation means the act or an instance of terminating, disallowing something. Cancellation may be related to anything, from canceling a trip to canceling an order. When canceling an order, a company or a customer needs to provide written confirmation of the same to avoid any legal issues with the provider.");
        trackings.add(tracking);

        tracking = new Tracking("22 Dec,09:30 PM", "Order cancelled this order is cancel after long Order cancelled we will be declarev then positive adfvv data add then display.");
        trackings.add(tracking);

        Tracking tracking1 = new Tracking("23 Dec,09:10 PM", "Order cancelled this order is cancel after long Order cancelled we will be declarev then positive adfvv data add then display.");
        trackings.add(tracking1);

        tracking = new Tracking("24 Dec,09:30 PM", "Order cancelled this order is cancel after long Order cancelled we will be declarev then positive adfvv data add then display.");
        trackings.add(tracking);

        Tracking tracking2 = new Tracking("27 Dec,09:10 PM", "Order cancelled");
        trackings.add(tracking2);

        tracking = new Tracking("28 Dec,09:30 PM", "Order cancelled this order is cancel after long Order cancelled we will be declarev then positive adfvv data add then display.");
        trackings.add(tracking);

        Tracking tracking3 = new Tracking("25 Dec,09:10 PM", "Order cancelled");
        trackings.add(tracking3);

        tracking = new Tracking("22 Dec,09:30 PM", "Order cancelled this order is cancel after long Order cancelled we will be declarev then positive adfvv data add then display sivisisvnv vjzdbjbjv reihgihginei erigisigning edinbnin einnvinvvreu rubeinvihrdnsudesr rubvd jznjz ussjndn sbaa knkndb mission kiliye khud impossible way to right.");
        trackings.add(tracking);

        trackingdetailsAdapter.notifyDataSetChanged();
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
