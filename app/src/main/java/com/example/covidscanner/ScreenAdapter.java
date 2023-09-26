package com.example.covidscanner;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
// Student Number : initials Surname
// 217011326  NEP Constable
// 217008056 MR Motingoe
// 217010608 ZR Khondlo
// 211118141 TR Sihlobo
public class ScreenAdapter extends RecyclerView.Adapter<ScreenAdapter.ViewHolder>
{
    private List<Screening> screenings;

    public ScreenAdapter(Context context, List<Screening> list)
    {
        screenings = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvDate, tvTemperature, tvAnswer1, tvAnswer2, tvAnswer3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // linking each component with widgets on the card view
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTemperature = itemView.findViewById(R.id.tvTemperature);
            tvAnswer1 = itemView.findViewById(R.id.tvAnswer1);
            tvAnswer2 = itemView.findViewById(R.id.tvAnswer2);
            tvAnswer3 = itemView.findViewById(R.id.tvAnswer3);
        }
    }

    @NonNull
    @Override
    public ScreenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //custom layout with recycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScreenAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(screenings.get(position));
        Date date = screenings.get(position).getCreated();
        double temperature = screenings.get(position).getTemperature();
        boolean symptoms = screenings.get(position).isHaveSymptoms();
        boolean riskCountry = screenings.get(position).isHighRiskCountry();
        boolean contactWithCovidPeople = screenings.get(position).isInContactWithCovidPeople();

        holder.tvDate.setText(date+ "");

        if(temperature > 37.8)
        {
            holder.tvTemperature.setTextColor(Color.RED);
            holder.tvTemperature.setText(temperature+" Degrees Celcius");
        }else {
            holder.tvTemperature.setText(temperature+" Degrees Celcius");
        }

        // setting the colour for each question
        if(symptoms == true)
        {
            holder.tvAnswer1.setTextColor(Color.GREEN);
            holder.tvAnswer1.setText("Yes");
        }
        else {
            holder.tvAnswer1.setTextColor(Color.RED);
            holder.tvAnswer1.setText("No");
        }

        if(riskCountry == true)
        {
            holder.tvAnswer2.setTextColor(Color.GREEN);
            holder.tvAnswer2.setText("Yes");
        }else {
            holder.tvAnswer2.setTextColor(Color.RED);
            holder.tvAnswer2.setText("No");
        }

        if(contactWithCovidPeople == true)
        {
            holder.tvAnswer3.setTextColor(Color.GREEN);
            holder.tvAnswer3.setText("Yes");
        }else {
            holder.tvAnswer3.setTextColor(Color.RED);
            holder.tvAnswer3.setText("No");
        }
    }

    @Override
    public int getItemCount() {
        return screenings.size();
    }

}
