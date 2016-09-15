package me.madri.vanilai.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import madri.me.vanilai.R;
import me.madri.vanilai.beans.HourlyDetailForecast;

/**
 * Created by badri on 7/24/16.
 */
public class HourlyForecastListAdapter extends RecyclerView.Adapter<HourlyForecastListAdapter.ForecastViewHolder> {
    private Context mContext;
    private List<HourlyDetailForecast> mHourlyDetailForecasts;

    public HourlyForecastListAdapter(Context context, List<HourlyDetailForecast> hourlyDetailForecasts) {
        this.mHourlyDetailForecasts = hourlyDetailForecasts;
        this.mContext = context;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_detail, parent, false);

        return new ForecastViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        HourlyDetailForecast hourlyDetailForecast = mHourlyDetailForecasts.get(position);
        Resources resources = mContext.getResources();
        holder.apparentTemperatureTextView.setText(resources.getString(R.string.apparent_temperature,
                hourlyDetailForecast.getApparentTemperature()));
        holder.imageView.setImageResource(getIconId(hourlyDetailForecast.getIcon()));
        holder.temperatureTextView.setText(hourlyDetailForecast.getTemperature());
        holder.summaryTextView.setText(hourlyDetailForecast.getSummary());
        SimpleDateFormat dateFormat = new SimpleDateFormat("h a", Locale.getDefault());
        String curTime = dateFormat.format(new Date(hourlyDetailForecast.getTime()*1000));
        holder.timeTextView.setText(curTime);
    }

    @Override
    public int getItemCount() {
        return mHourlyDetailForecasts.size();
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView timeTextView;
        protected TextView summaryTextView;
        protected TextView temperatureTextView;
        protected TextView apparentTemperatureTextView;

        public ForecastViewHolder(View view) {
            super(view);
            this.timeTextView = (TextView) view.findViewById(R.id.list_detail_time);
            this.imageView = (ImageView) view.findViewById(R.id.list_detail_image);
            this.summaryTextView = (TextView) view.findViewById(R.id.list_detail_summary);
            this.temperatureTextView = (TextView) view.findViewById(R.id.list_detail_temperature);
            this.apparentTemperatureTextView = (TextView) view.findViewById(R.id.list_detail_apparentTemperature);
        }
    }

    public static int getIconId(String iconString) {
        int iconId = R.drawable.clear_day;
        if(iconString.equalsIgnoreCase("clear-day")) {
            iconId = R.drawable.clear_day;
        } else if(iconString.equalsIgnoreCase("clear-night")) {
            iconId = R.drawable.clear_night;
        } else if(iconString.equalsIgnoreCase("rain")) {
            iconId = R.drawable.rain;
        } else if(iconString.equalsIgnoreCase("snow")) {
            iconId = R.drawable.snow;
        } else if(iconString.equalsIgnoreCase("sleet")) {
            iconId = R.drawable.sleet;
        } else if(iconString.equalsIgnoreCase("wind")) {
            iconId = R.drawable.wind;
        } else if(iconString.equalsIgnoreCase("fog")) {
            iconId = R.drawable.fog;
        } else if(iconString.equalsIgnoreCase("cloudy")) {
            iconId = R.drawable.cloudy;
        } else if(iconString.equalsIgnoreCase("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        } else if(iconString.equalsIgnoreCase("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }
        return iconId;
    }
}
