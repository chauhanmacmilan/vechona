package com.vechona.com.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vechona.app.R;
import com.vechona.com.ui.model.Tracking;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackingdetailsAdapter extends RecyclerView.Adapter<TrackingdetailsAdapter.MyViewHolder> {
    private List<Tracking> trackings;
    private Context context;

    public TrackingdetailsAdapter(List<Tracking> trackings, Context context) {
        this.trackings = trackings;
        this.context = context;
    }

    @NonNull
    @Override
    public TrackingdetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tracking_item, viewGroup, false);
        return new TrackingdetailsAdapter.MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Tracking tracking = trackings.get(position);
        holder.tvTrackingDate.setText(tracking.getDate());
        holder.tvTrackingDescription.setText(tracking.getDescription());

        if (position == 0) {
            holder.tvTrackingDate.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvTrackingDescription.setTextColor(context.getResources().getColor(R.color.red));
            holder.roundImage.setColorFilter(context.getResources().getColor(R.color.red));
            holder.dashImage.setColorFilter(context.getResources().getColor(R.color.red));
        } else {
            holder.tvTrackingDate.setTextColor(context.getResources().getColor(R.color.grey_80));
            holder.tvTrackingDescription.setTextColor(context.getResources().getColor(R.color.grey_80));
            holder.roundImage.setColorFilter(context.getResources().getColor(R.color.grey_80));
            holder.dashImage.setColorFilter(context.getResources().getColor(R.color.grey_80));
        }
    }

    @Override
    public int getItemCount() {
        return trackings.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTrackingDate)
        TextView tvTrackingDate;

        @BindView(R.id.tvTrackingDescription)
        TextView tvTrackingDescription;

        @BindView(R.id.roundImage)
        AppCompatImageView roundImage;

        @BindView(R.id.dashImage)
        AppCompatImageView dashImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
