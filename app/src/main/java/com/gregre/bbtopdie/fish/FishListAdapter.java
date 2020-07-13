package com.gregre.bbtopdie.fish;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gregre.bbtopdie.R;
import com.gregre.bbtopdie.db.Fish;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FishListAdapter extends RecyclerView.Adapter<FishListAdapter.FishViewHolder> {
    class FishViewHolder extends RecyclerView.ViewHolder {
        private final TextView fishItemView;
        private final TextView fishPriceView;
        private final TextView fishTimeView;
        private final TextView fishPeriodView;
        private final TextView fishLocationView;
        private final TextView fishRarityView;
        private final TextView fishShadowView;

        private final ImageView imageView;

        private FishViewHolder(View itemView) {
            super(itemView);
            fishItemView = itemView.findViewById(R.id.fishTextView);
            fishPriceView = itemView.findViewById(R.id.fishPriceView);
            fishTimeView = itemView.findViewById(R.id.fishTimeView);
            fishPeriodView = itemView.findViewById(R.id.fishPeriodView);
            fishLocationView = itemView.findViewById(R.id.fishLocationView);
            fishRarityView = itemView.findViewById(R.id.fishRarityView);
            fishShadowView = itemView.findViewById(R.id.fishShadowView);
            imageView = itemView.findViewById(R.id.fishImageView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Fish> mFishes; // Cached copy of fishes

    public FishListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public FishListAdapter.FishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fish_recyclerview_item, parent, false);
        return new FishViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FishListAdapter.FishViewHolder holder, int position) {
        if (mFishes != null) {
            Fish current = mFishes.get(position);
            holder.fishItemView.setText(current.getName_fr());
            holder.fishRarityView.setText(rarityToString(current.getRarity()));
            holder.fishShadowView.setText(shadowToString(current.getShadow()));
            holder.fishPriceView.setText(current.getPrice() + " cloch.");
            if(current.isIs_all_day()) {
                holder.fishTimeView.setText("Toute la journée");
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

                holder.fishTimeView.setText(time);
            }
            if(current.isIs_all_year()) {
                holder.fishPeriodView.setText("Toute l'année");
            } else {
                String period = current.getPeriod_north();

                Pattern p = Pattern.compile("([0-9]+)");
                Matcher m = p.matcher(period);

                while (m.find()) {
                    int month = Integer.parseInt(m.group(1));
                    period = m.replaceFirst(monthToString(month));
                    m = p.matcher(period);
                }

                holder.fishPeriodView.setText(period);
            }
            holder.fishLocationView.setText(locationTranslation(current.getLocation()));

            int resID = getResId(current.getName(), R.drawable.class);
            holder.imageView.setImageResource(resID);

        } else {
            // Covers the case of data not being ready yet.
            holder.fishItemView.setText("No Fish");
            holder.fishPriceView.setText("0");
            holder.fishTimeView.setText("None");
            holder.fishPeriodView.setText("None");
            holder.fishLocationView.setText("None");
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

    public void setFishes(List<Fish> fishes) {
        mFishes = fishes;
        notifyDataSetChanged();
    }

    public String locationTranslation(String loc_en) {
        switch (loc_en) {
            case "Pond":
                return "Étang";
            case "River":
                return "Rivière";
            case "River (Clifftop)":
                return "Rivière (hauteurs)";
            case "River (Clifftop) & Pond":
                return "Rivière (hauteurs) & étang";
            case "River (mouth)":
                return "Rivière (embouchure)";
            case "Sea":
                return "Océan";
            case "Pier":
                return "Ponton";
            case "Sea (when raining or snowing)":
                return "Océan (pluie ou neige)";
            default:
                return "";
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

    public String shadowToString(String shadow) {
        switch (shadow) {
            case "Smallest (1)":
                return "Minuscule (1)";
            case "Small (2)":
                return "Petite (2)";
            case "Medium (3)":
                return "Moyenne (3)";
            case "Medium (4)":
                return "Moyenne (4)";
            case "Medium with fin (4)":
                return "Moyenne avec un aileron";
            case "Large (4)":
                return "Large (4)";
            case "Large (5)":
                return "Large (5)";
            case "Largest (6)":
                return "Immense";
            case "Largest with fin (6)":
                return "Immense avec un aileron";
            case "Narrow":
                return "Fine";
            default:
                return "##############";
        }
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
