package com.gregre.bbtopdie.sea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gregre.bbtopdie.R;
import com.gregre.bbtopdie.db.SeaCreature;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeaListAdapter extends RecyclerView.Adapter<SeaListAdapter.SeaViewHolder> {

    class SeaViewHolder extends RecyclerView.ViewHolder {
        private final TextView seaItemView;
        private final TextView seaPriceView;
        private final TextView seaTimeView;
        private final TextView seaPeriodView;
        private final TextView seaSpeedView;
        private final TextView seaShadowView;
        private final ImageView imageView;

        private SeaViewHolder(View itemView) {
            super(itemView);
            seaItemView = itemView.findViewById(R.id.seaTextView);
            seaPriceView = itemView.findViewById(R.id.seaPriceView);
            seaTimeView = itemView.findViewById(R.id.seaTimeView);
            seaPeriodView = itemView.findViewById(R.id.seaPeriodView);
            seaSpeedView = itemView.findViewById(R.id.seaSpeedView);
            seaShadowView = itemView.findViewById(R.id.seaShadowView);
            imageView = itemView.findViewById(R.id.seaImageView);
        }
    }

    private final LayoutInflater mInflater;
    private List<SeaCreature> seaCreatures; // Cached copy of seas

    public SeaListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public SeaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.sea_recyclerview_item, parent, false);
        return new SeaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SeaViewHolder holder, int position) {
        if (seaCreatures != null) {
            SeaCreature current = seaCreatures.get(position);
            holder.seaItemView.setText(current.getName_fr());
            holder.seaSpeedView.setText(speedToString(current.getSpeed()));
            holder.seaShadowView.setText(shadowToString(current.getShadow()));
            holder.seaPriceView.setText(current.getPrice() + " cloch.");
            if(current.isIs_all_day()) {
                holder.seaTimeView.setText("Toute la journée");
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

                holder.seaTimeView.setText(time);
            }
            if(current.isIs_all_year()) {
                holder.seaPeriodView.setText("Toute l'année");
            } else {
                String period = current.getPeriod_north();

                Pattern p = Pattern.compile("([0-9]+)");
                Matcher m = p.matcher(period);

                while (m.find()) {
                    int month = Integer.parseInt(m.group(1));
                    period = m.replaceFirst(monthToString(month));
                    m = p.matcher(period);
                }

                holder.seaPeriodView.setText(period);
            }

            int resID = getResId(current.getName(), R.drawable.class);
            holder.imageView.setImageResource(resID);

        } else {
            // Covers the case of data not being ready yet.
            holder.seaItemView.setText("No Sea");
            holder.seaPriceView.setText("0");
            holder.seaTimeView.setText("None");
            holder.seaPeriodView.setText("None");
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
                return "############";
        }
    }

    public String speedToString(String speed) {
        switch (speed) {
            case "Stationary":
                return "Stationaire";
            case "Very slow":
                return "Très lent";
            case "Slow":
                return "Lent";
            case "Medium":
                return "Moyenne";
            case "Fast":
                return "Rapide";
            case "Very fast":
                return "Très rapide";
            default:
                return "";
        }
    }

    public String shadowToString(String shadow) {
        switch (shadow) {
            case "Smallest":
                return "Très petite";
            case "Small":
                return "Petite";
            case "Medium":
                return "Moyenne";
            case "Large":
                return "Large";
            case "Largest":
                return "Très large";
            default:
                return "############";
        }
    }

    public void setSeaCreatures(List<SeaCreature> seaCreatures) {
        this.seaCreatures = seaCreatures;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mSeas has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (seaCreatures != null)
            return seaCreatures.size();
        else return 0;
    }
}