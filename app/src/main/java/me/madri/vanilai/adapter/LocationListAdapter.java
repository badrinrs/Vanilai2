package me.madri.vanilai.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import madri.me.vanilai.R;
import me.madri.vanilai.beans.City;

/**
 * Created by bnara on 7/30/2016.
 */
public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.LocationViewHolder> {

    private List<City> mCities;
    private Context mContext;

    public LocationListAdapter(Context context, List<City> cities) {
        mCities = cities;
        mContext = context;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_list_layout, parent, false);

        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        City city = mCities.get(position);
        Resources resources = mContext.getResources();
        holder.mCityName.setText(city.getCity());
        holder.mCurrentTemperature.setText(resources.getString(R.string.current_temperature, city.getTemperature(), "\u00b0F"));
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        protected TextView mCityName;
        protected ImageView mCurrentWeatherImage;
        protected TextView mCurrentTemperature;
        public LocationViewHolder(View view) {
            super(view);
            mCityName = (TextView) view.findViewById(R.id.locationName);
            mCurrentTemperature = (TextView) view.findViewById(R.id.temperature);
            mCurrentWeatherImage = (ImageView) view.findViewById(R.id.currentWeatherImage);
        }
    }
}
