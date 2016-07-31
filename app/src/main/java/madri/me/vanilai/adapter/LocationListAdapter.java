package madri.me.vanilai.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import madri.me.vanilai.R;
import madri.me.vanilai.beans.City;

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
                .inflate(R.layout.weekly_forecast_detail, parent, false);

        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        City city = mCities.get(position);
        Resources resources = mContext.getResources();
        holder.mCityName.setText(city.getCity());
        holder.mMinTemp.setText(resources.getString(R.string.min_temperature, city.getMinTemperature()));
        holder.mMinTemp.setText(resources.getString(R.string.max_temperature, city.getMaxTemperature()));
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        protected TextView mCityName;
        protected TextView mMaxTemp;
        protected TextView mMinTemp;
        public LocationViewHolder(View view) {
            super(view);
            mCityName = (TextView) view.findViewById(R.id.locationName);
            mMaxTemp = (TextView) view.findViewById(R.id.max_temperature);
            mMinTemp = (TextView) view.findViewById(R.id.min_temperature);
        }
    }
}
