package com.example.cekcuaca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {

    private Context context;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;

    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModal> weatherRVModalArrayList) {
        this.context = context;
        this.weatherRVModalArrayList = weatherRVModalArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherRVModal modal = weatherRVModalArrayList.get(position);
        holder.temperatureTV.setText(modal.getTemperature() + "°C");
        holder.windSpeedTV.setText(modal.getWindSpeed() + " m/s");
        holder.timeTV.setText(modal.getTime());

        // Load weather icon using Picasso
        String iconUrl = "https://www.weatherbit.io/static/img/icons/" + modal.getIcon() + ".png";
        Picasso.get().load(iconUrl).into(holder.conditionIV);
    }

    @Override
    public int getItemCount() {
        return weatherRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView temperatureTV, windSpeedTV, timeTV;
        private ImageView conditionIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temperatureTV = itemView.findViewById(R.id.idTVTemperature);
            windSpeedTV = itemView.findViewById(R.id.idTVWindSpeed);
            timeTV = itemView.findViewById(R.id.idTVTime);
            conditionIV = itemView.findViewById(R.id.idIVCondition);
        }
    }
}
