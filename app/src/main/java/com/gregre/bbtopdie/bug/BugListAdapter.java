package com.gregre.bbtopdie.bug;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gregre.bbtopdie.R;
import com.gregre.bbtopdie.db.Bug;

import java.util.List;


public class BugListAdapter extends RecyclerView.Adapter<BugListAdapter.BugViewHolder> {

    class BugViewHolder extends RecyclerView.ViewHolder {
        private final TextView bugItemView;
        private final TextView bugPriceView;
        private final TextView bugTimeView;
        private final TextView bugPeriodView;

        private BugViewHolder(View itemView) {
            super(itemView);
            bugItemView = itemView.findViewById(R.id.textView);
            bugPriceView = itemView.findViewById(R.id.priceView);
            bugTimeView = itemView.findViewById(R.id.timeView);
            bugPeriodView = itemView.findViewById(R.id.periodView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Bug> mBugs; // Cached copy of bugs

    public BugListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BugViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new BugViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BugViewHolder holder, int position) {
        if (mBugs != null) {
            Bug current = mBugs.get(position);
            holder.bugItemView.setText(current.getName());
            holder.bugPriceView.setText(current.getPrice());
            holder.bugTimeView.setText(current.getTime());
            holder.bugPeriodView.setText(current.getPeriod());
        } else {
            // Covers the case of data not being ready yet.
            holder.bugItemView.setText("No Bug");
            holder.bugPriceView.setText("0");
            holder.bugTimeView.setText("None");
            holder.bugPeriodView.setText("None");
        }
    }

    public void setBugs(List<Bug> bugs) {
        mBugs = bugs;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mBugs has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mBugs != null)
            return mBugs.size();
        else return 0;
    }
}