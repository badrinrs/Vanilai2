package me.madri.vanilai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import madri.me.vanilai.R;
import me.madri.vanilai.beans.EarthquakeInformation;
import me.madri.vanilai.beans.EarthquakeProperties;

/**
 * Created by badri on 7/24/16.
 */
public class EarthquakeListAdapter extends RecyclerView.Adapter<EarthquakeListAdapter.EarthquakeViewHolder>{

    private List<EarthquakeProperties> mEarthquakePropertiesList;
    private Context mContext;

    public EarthquakeListAdapter(Context context, List<EarthquakeProperties> earthquakeInformationList) {
        this.mContext = context;
        this.mEarthquakePropertiesList = earthquakeInformationList;
    }

    @Override
    public EarthquakeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.earthquake_list_detail, parent, false);

        return new EarthquakeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EarthquakeViewHolder holder, int position) {
        EarthquakeProperties earthquakeProperties = mEarthquakePropertiesList.get(position);
        EarthquakeInformation earthquakeInformation = earthquakeProperties.getEarthquakeInformation();
        holder.mMagnitude.setText("Mag: "+earthquakeInformation.getMagnitude());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd h:mm a", Locale.getDefault());
        long time = earthquakeInformation.getTime();
        holder.mTime.setText(simpleDateFormat.format(new Date(time)));
        holder.mPlace.setText(earthquakeInformation.getPlace());
    }

    @Override
    public int getItemCount() {
        return mEarthquakePropertiesList.size();
    }

    public class EarthquakeViewHolder extends RecyclerView.ViewHolder {
        protected TextView mMagnitude;
        protected TextView mPlace;
        protected TextView mTime;
        public EarthquakeViewHolder(View itemView) {
            super(itemView);

            this.mMagnitude = (TextView) itemView.findViewById(R.id.magnitude);
            this.mPlace = (TextView) itemView.findViewById(R.id.location);
            this.mTime = (TextView) itemView.findViewById(R.id.earthquake_time);
        }
    }
}
