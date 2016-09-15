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
import me.madri.vanilai.beans.WeeklyDetailForecast;

/**
 * Created by badri on 7/24/16.
 */
public class WeeklyForecastAdapter extends RecyclerView.Adapter<WeeklyForecastAdapter.WeeklyForecastViewHolder> {
    private Context mContext;
    private List<WeeklyDetailForecast> mWeeklyDetailForecasts;

    public WeeklyForecastAdapter(Context context, List<WeeklyDetailForecast> weeklyDetailForecasts) {
        this.mWeeklyDetailForecasts = weeklyDetailForecasts;
        this.mContext = context;
    }

    @Override
    public WeeklyForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weekly_forecast_detail, parent, false);

        return new WeeklyForecastViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WeeklyForecastViewHolder holder, int position) {
        WeeklyDetailForecast weeklyDetailForecast = mWeeklyDetailForecasts.get(position);
        Resources resources = mContext.getResources();
        holder.mMaxTemperature.setText(resources.getString(R.string.max_temperature, weeklyDetailForecast.getMaxTemperature()));
        holder.mWeatherImage.setImageResource(getIconId(weeklyDetailForecast.getIcon()));
        holder.mMinTemperature.setText(resources.getString(R.string.min_temperature, weeklyDetailForecast.getMinTemperature()));
        holder.mSummary.setText(weeklyDetailForecast.getSummary());
        SimpleDateFormat dateFormat = new SimpleDateFormat("E", Locale.getDefault());
        String curTime = dateFormat.format(new Date(weeklyDetailForecast.getTime()*1000));
        holder.mDay.setText(curTime);
    }

    @Override
    public int getItemCount() {
        return mWeeklyDetailForecasts.size();
    }

    public class WeeklyForecastViewHolder extends RecyclerView.ViewHolder {
        protected TextView mMaxTemperature;
        protected TextView mMinTemperature;
        protected TextView mSummary;
        protected TextView mDay;
        protected ImageView mWeatherImage;
        public WeeklyForecastViewHolder(View itemView) {
            super(itemView);
            mWeatherImage = (ImageView) itemView.findViewById(R.id.weather_image);
            mMaxTemperature = (TextView) itemView.findViewById(R.id.max_temperature);
            mMinTemperature = (TextView) itemView.findViewById(R.id.min_temperature);
            mSummary = (TextView) itemView.findViewById(R.id.summary);
            mDay = (TextView) itemView.findViewById(R.id.day);
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
