package com.gregre.bbtopdie.fish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gregre.bbtopdie.R;
import com.gregre.bbtopdie.db.Fish;

import java.util.List;

public class FishListAdapter extends RecyclerView.Adapter<FishListAdapter.FishViewHolder> {
    class FishViewHolder extends RecyclerView.ViewHolder {
        private final TextView fishItemView;
        private final TextView fishPriceView;
        private final TextView fishTimeView;
        private final TextView fishPeriodView;
        private final TextView fishPlaceView;

        private FishViewHolder(View itemView) {
            super(itemView);
            fishItemView = itemView.findViewById(R.id.fishTextView);
            fishPriceView = itemView.findViewById(R.id.fishPriceView);
            fishTimeView = itemView.findViewById(R.id.fishTimeView);
            fishPeriodView = itemView.findViewById(R.id.fishPeriodView);
            fishPlaceView = itemView.findViewById(R.id.fishPlaceView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Fish> mFishes; // Cached copy of fishs

    public FishListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public FishListAdapter.FishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fish_recyclerview_item, parent, false);
        return new FishListAdapter.FishViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FishListAdapter.FishViewHolder holder, int position) {
        if (mFishes != null) {
            Fish current = mFishes.get(position);
            holder.fishItemView.setText(current.getName());
            holder.fishPriceView.setText(current.getPrice());
            holder.fishTimeView.setText(current.getTime());
            holder.fishPeriodView.setText(current.getPeriod());
            holder.fishPlaceView.setText(current.getPlace());
        } else {
            // Covers the case of data not being ready yet.
            holder.fishItemView.setText("No Fish");
            holder.fishPriceView.setText("0");
            holder.fishTimeView.setText("None");
            holder.fishPeriodView.setText("None");
            holder.fishPlaceView.setText("None");
        }
    }

    public void setFishes(List<Fish> fishes) {
        mFishes = fishes;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mFishes has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mFishes != null)
            return mFishes.size();
        else return 0;
    }
}
