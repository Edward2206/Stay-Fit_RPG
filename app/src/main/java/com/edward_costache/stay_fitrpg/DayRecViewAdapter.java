package com.edward_costache.stay_fitrpg;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DayRecViewAdapter extends RecyclerView.Adapter<DayRecViewAdapter.ViewHolder> {

    private ArrayList<ProgressDay> arrayDays = new ArrayList<>();
    private ArrayList<String> arrayDaysNames = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dayName.setText(arrayDaysNames.get(position));
        holder.pushupCount.setText(Integer.toString(arrayDays.get(position).getPushups()));
        holder.situpCount.setText(Integer.toString(arrayDays.get(position).getSitups()));
        holder.squatCount.setText(Integer.toString(arrayDays.get(position).getSquats()));
        holder.distanceRan.setText(Double.toString(arrayDays.get(position).getDistance()));
    }

    @Override
    public int getItemCount() {
        return arrayDays.size();
    }

    public void setDays(ArrayList<String> names, ArrayList<ProgressDay> days)
    {
        this.arrayDays = days;
        this.arrayDaysNames = names;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView dayName, pushupCount, situpCount, squatCount, distanceRan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.dayItemTxtDayName);
            pushupCount = itemView.findViewById(R.id.dayListItemTxtPushupCount);
            situpCount = itemView.findViewById(R.id.dayListItemTxtSitupCount);
            squatCount = itemView.findViewById(R.id.dayListItemTxtSquatCount);
            distanceRan = itemView.findViewById(R.id.dayListItemTxtDistanceRan);
        }
    }
}
