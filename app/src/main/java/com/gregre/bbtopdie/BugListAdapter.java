package com.gregre.bbtopdie;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class BugListAdapter extends RecyclerView.Adapter<BugListAdapter.BugViewHolder> {

    class BugViewHolder extends RecyclerView.ViewHolder {
        private final TextView bugItemView;

        private BugViewHolder(View itemView) {
            super(itemView);
            bugItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Bug> mBugs; // Cached copy of bugs

    BugListAdapter(Context context) {
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
        } else {
            // Covers the case of data not being ready yet.
            holder.bugItemView.setText("No Bug");
        }
    }

    void setBugs(List<Bug> bugs) {
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