package madri.me.vanilai.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import madri.me.vanilai.R;
import madri.me.vanilai.activity.VanilaiNewLocationActivity;
import madri.me.vanilai.beans.City;

/**
 * Created by badri on 7/31/16.
 */
public class LocationListDialogAdapter extends RecyclerView.Adapter<LocationListDialogAdapter.LocationViewHolder> {

    private List<City> mCities;
    private Context mContext;

    public LocationListDialogAdapter(Context context, List<City> cities) {
        mCities = cities;
        mContext = context;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_list_dialog_layout, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        City city = mCities.get(position);
        holder.mLocation.setText(city.getCity());
        holder.mLatitude = city.getLatitude();
        holder.mLongitude = city.getLongitude();
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        protected TextView mLocation;
        protected double mLatitude;
        protected double mLongitude;
        public LocationViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mLocation = (TextView) itemView.findViewById(R.id.location);
        }

        @Override
        public void onClick(View view) {
            Intent locationIntent = new Intent(mContext, VanilaiNewLocationActivity.class);
            locationIntent.putExtra("latitude", mLatitude);
            locationIntent.putExtra("longitude", mLongitude);
            locationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(locationIntent);
        }

        @Override
        public boolean onLongClick(View view) {
            Toast.makeText(mContext, mLocation.getText().toString()+" Long Clicked", Toast.LENGTH_LONG).show();
            return true;
        }
    }
}
