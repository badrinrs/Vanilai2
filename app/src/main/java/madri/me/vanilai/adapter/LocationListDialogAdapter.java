package madri.me.vanilai.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import madri.me.vanilai.R;
import madri.me.vanilai.activity.VanilaiNewLocationActivity;
import madri.me.vanilai.beans.City;
import madri.me.vanilai.db.VanilaiSqlLiteHelper;

/**
 * Created by badri on 7/31/16.
 */
public class LocationListDialogAdapter extends RecyclerView.Adapter<LocationListDialogAdapter.LocationViewHolder> {

    private List<City> mCities;
    private Context mContext;
    private VanilaiSqlLiteHelper mHelper;

    public LocationListDialogAdapter(Context context, List<City> cities, VanilaiSqlLiteHelper helper) {
        mCities = cities;
        mContext = context;
        mHelper = helper;
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
        public boolean onLongClick(final View view) {
            MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(view.getContext())
                    .autoDismiss(true).title("Remove City: ").content(mLocation.getText().toString())
                    .negativeText("Cancel").positiveText("Remove")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    }).onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            String cityName = mLocation.getText().toString();
                            City removalCity = null;
                            for(City city: mCities) {
                                if(city.getCity().equals(cityName)) {
                                    removalCity = city;
                                }
                            }
                            if(removalCity != null) {
                                mCities.remove(removalCity);
                                mHelper.removeCity(removalCity.getCity());
                                LocationListDialogAdapter.this.notifyDataSetChanged();
                            }

                        }
                    });
            dialogBuilder.show();
            return true;
        }
    }
}
