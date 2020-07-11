package com.gregre.bbtopdie.bug;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gregre.bbtopdie.R;
import com.gregre.bbtopdie.db.Bug;

import java.lang.reflect.Field;
import java.util.List;

public class BugListAdapter extends RecyclerView.Adapter<BugListAdapter.BugViewHolder> {

    class BugViewHolder extends RecyclerView.ViewHolder {
        private final TextView bugItemView;
        private final TextView bugPriceView;
        private final TextView bugTimeView;
        private final TextView bugPeriodView;
        private final ImageView imageView;

        private BugViewHolder(View itemView) {
            super(itemView);
            bugItemView = itemView.findViewById(R.id.bugTextView);
            bugPriceView = itemView.findViewById(R.id.bugPriceView);
            bugTimeView = itemView.findViewById(R.id.bugTimeView);
            bugPeriodView = itemView.findViewById(R.id.bugPeriodView);
            imageView = itemView.findViewById(R.id.bugImageView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Bug> mBugs; // Cached copy of bugs

    public BugListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public BugViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.bug_recyclerview_item, parent, false);
        return new BugViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BugViewHolder holder, int position) {
        if (mBugs != null) {
            Bug current = mBugs.get(position);
            holder.bugItemView.setText(current.getName());
            holder.bugPriceView.setText(current.getPrice() + " cloch.");
            if(current.isIs_all_day()) {
                holder.bugTimeView.setText("Toute la journée");
            } else {
                //TODO
                String time = current.getTime();

                holder.bugTimeView.setText(0 + "h - " + 0 + "h");
            }
            if(current.isIs_all_year()) {
                holder.bugPeriodView.setText("Toute l'année");
            } else {
                //TODO
                holder.bugPeriodView.setText(periodToString(1) + " - " + periodToString(1));
            }

            int resID = getResId("bug" + current.getId(), R.drawable.class);
            holder.imageView.setImageResource(resID);

        } else {
            // Covers the case of data not being ready yet.
            holder.bugItemView.setText("No Bug");
            holder.bugPriceView.setText("0");
            holder.bugTimeView.setText("None");
            holder.bugPeriodView.setText("None");
        }
    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String periodToString(int period) {
        switch (period) {
            case 1:
                return "Janvier";
            case 2:
                return "Février";
            case 3:
                return "Mars";
            case 4:
                return "Avril";
            case 5:
                return "Mai";
            case 6:
                return "Juin";
            case 7:
                return "Juillet";
            case 8:
                return "Août";
            case 9:
                return "Septembre";
            case 10:
                return "Octobre";
            case 11:
                return "Novembre";
            case 12:
                return "Décembre";
            default:
                return "";
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