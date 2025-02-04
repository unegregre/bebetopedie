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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BugListAdapter extends RecyclerView.Adapter<BugListAdapter.BugViewHolder> {

    class BugViewHolder extends RecyclerView.ViewHolder {
        private final TextView bugItemView;
        private final TextView bugPriceView;
        private final TextView bugTimeView;
        private final TextView bugPeriodView;
        private final TextView bugLocationView;
        private final TextView bugRarityView;
        private final ImageView imageView;

        private BugViewHolder(View itemView) {
            super(itemView);
            bugItemView = itemView.findViewById(R.id.bugTextView);
            bugPriceView = itemView.findViewById(R.id.bugPriceView);
            bugTimeView = itemView.findViewById(R.id.bugTimeView);
            bugPeriodView = itemView.findViewById(R.id.bugPeriodView);
            bugLocationView = itemView.findViewById(R.id.bugLocationView);
            bugRarityView = itemView.findViewById(R.id.bugRarityView);
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
            holder.bugItemView.setText(current.getName_fr());
            holder.bugLocationView.setText(locationToString(current.getLocation()));
            holder.bugRarityView.setText(rarityToString(current.getRarity()));
            holder.bugPriceView.setText(current.getPrice() + " cloch.");
            if(current.isIs_all_day()) {
                holder.bugTimeView.setText("Toute la journée");
            } else {
                String time = current.getTime();

                Pattern p = Pattern.compile("([0-9]+)pm");
                Matcher m = p.matcher(time);

                while (m.find()) {
                    int hour_pm = Integer.parseInt(m.group(1))+12;
                    time = m.replaceFirst(hour_pm+"h");
                    m = p.matcher(time);
                }
                p = Pattern.compile("am");
                m = p.matcher(time);
                time = m.replaceAll("h");

                holder.bugTimeView.setText(time);
            }
            if(current.isIs_all_year()) {
                holder.bugPeriodView.setText("Toute l'année");
            } else {
                String period = current.getPeriod_north();

                Pattern p = Pattern.compile("([0-9]+)");
                Matcher m = p.matcher(period);

                while (m.find()) {
                    int month = Integer.parseInt(m.group(1));
                    period = m.replaceFirst(monthToString(month));
                    m = p.matcher(period);
                }

                holder.bugPeriodView.setText(period);
            }

            int resID = getResId(current.getName(), R.drawable.class);
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

    public String monthToString(int period) {
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

    public String locationToString(String location) {
        switch(location) {
            case "Flying":
                return "Voletant";
            case "Flying near hybrid flowers":
                return "Voletant près des hybrides";
            case "Flying by light":
                return "Voletant près des lumières";
            case "On trees":
                return "Sur les arbres";
            case "On the ground":
                return "Sur le sol";
            case "On flowers":
                return "Sur les fleurs";
            case "On white flowers":
                return "Sur les fleurs blanches";
            case "Underground":
                return "Sous le sol";
            case "On ponds and rivers":
                return "Dans les étangs et rivières";
            case "On tree stumps":
                return "Sur les souches";
            case "On palm trees":
                return "Sur les cocotiers";
            case "Under trees":
                return "Au pied des arbres";
            case "On rotten food":
                return "Sur un navet pourri";
            case "On the beach":
                return "Sur la plage";
            case "On beach rocks":
                return "Sur les rochers près de l'océan";
            case "Near trash":
                return "Près des ordures";
            case "On villagers":
                return "Sur les habitants";
            case "On rocks (when raining)":
                return "Sur les rochers (pluie)";
            case "Hitting rocks":
                return "En frappant un rocher";
            default:
                return "En secouant un arbre";

        }
    }

    public String rarityToString(String rarity) {
        switch(rarity) {
            case "Common":
                return "Commun";
            case "Uncommon":
                return "Peu commun";
            case "Rare":
                return "Rare";
            case "Ultra-rare":
                return "Ultra rare";
            default:
                return "############";

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