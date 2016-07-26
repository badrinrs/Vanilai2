package madri.me.vanilai.fragment;

/**
 * Created by badri on 7/23/16.
 */

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import madri.me.vanilai.R;
import madri.me.vanilai.activity.EarthquakeActivity;
import madri.me.vanilai.activity.ForecastActivity;
import madri.me.vanilai.beans.CurrentForecast;
import madri.me.vanilai.beans.Earthquake;
import madri.me.vanilai.beans.Forecast;
import madri.me.vanilai.beans.HourlyDetailForecast;
import madri.me.vanilai.beans.HourlyForecast;
import madri.me.vanilai.helper.AddressHelper;
import madri.me.vanilai.service.EarthquakeService;
import madri.me.vanilai.service.VanilaiWeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAG = PlaceholderFragment.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLocation;

    private static TextView mLocationTextView;
    private Button mEarthquakeDetected;
    private TextView mTemperature;
    private TextView mHumidity;
    private TextView mPrecipitation;
    private TextView mSummary;
    private TextView mCurrentTime;
    private ImageView mIconImage;
    private ImageButton mRefresh;
    private Button mHourlyButton;
    private Button mWeeklyButton;
    private Switch mTemperatureSwitch;
    private Earthquake mEarthquake;


    private static final String ARG_SECTION_NUMBER = "section_number";
    private Forecast mForecast;


    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vanilai, container, false);
        mLocationTextView = (TextView) rootView.findViewById(R.id.location);
        mTemperature = (TextView) rootView.findViewById(R.id.temperature);
        mEarthquakeDetected = (Button) rootView.findViewById(R.id.earthquakeDetected);
        mHumidity = (TextView) rootView.findViewById(R.id.humidity);
        mPrecipitation = (TextView) rootView.findViewById(R.id.precipitation);
        mSummary = (TextView) rootView.findViewById(R.id.weatherDescription);
        mCurrentTime = (TextView) rootView.findViewById(R.id.currentTime);
        mIconImage = (ImageView) rootView.findViewById(R.id.currentWeatherImage);
        mRefresh = (ImageButton) rootView.findViewById(R.id.refresh);
        mHourlyButton = (Button) rootView.findViewById(R.id.hourlyForecast);
        mWeeklyButton = (Button) rootView.findViewById(R.id.weeklyForecastButton);
        mTemperatureSwitch = (Switch) rootView.findViewById(R.id.temperatureSwitch);
        mTemperatureSwitch.setText("\u00b0F");

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds

        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getForecast(mLocation);
                getEarthquakeInformation(mLocation);
            }
        });


        mTemperatureSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setOnTemperatureSwitch();
            }
        });

        mWeeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mForecast!=null) {
                    Intent forecastIntent = new Intent(getActivity(), ForecastActivity.class);
                    Gson gson = new Gson();
                    String target = gson.toJson(mForecast);
                    forecastIntent.putExtra("forecast", target);
                    forecastIntent.putExtra("city", mLocationTextView.getText().toString());
                    forecastIntent.putExtra("forecastType", "weekly");
                    startActivity(forecastIntent);
                } else {
                    Log.e(TAG, "Weekly Forecasts not obtained Yet! Please try again later!");
                    Toast.makeText(getActivity(), "Weekly Forecasts not obtained Yet! Please try again later!", Toast.LENGTH_LONG).show();
                }
            }
        });

        mHourlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mForecast!=null) {
                    Intent forecastIntent = new Intent(getActivity(), ForecastActivity.class);
                    Gson gson = new Gson();
                    String target = gson.toJson(mForecast);
                    forecastIntent.putExtra("forecast", target);
                    forecastIntent.putExtra("city", mLocationTextView.getText().toString());
                    forecastIntent.putExtra("forecastType", "hourly");
                    startActivity(forecastIntent);
                } else {
                    Log.e(TAG, "Hourly Forecasts not obtained Yet! Please try again later!");
                    Toast.makeText(getActivity(), "Hourly Forecasts not obtained Yet! Please try again later!", Toast.LENGTH_LONG).show();
                }
            }
        });
        mEarthquakeDetected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent earthquakeIntent = new Intent(getActivity(), EarthquakeActivity.class);
                Gson gson = new Gson();
                String target = gson.toJson(mEarthquake);
                earthquakeIntent.putExtra("earthquake", target);
                earthquakeIntent.putExtra("city", mLocationTextView.getText().toString());
                startActivity(earthquakeIntent);
            }
        });

        return rootView;
    }

    private void getEarthquakeInformation(Location location) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://earthquake.usgs.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EarthquakeService earthquakeService = retrofit.create(EarthquakeService.class);
        Call<Earthquake> earthquakeCall = earthquakeService.getEarthquake("geojson", location.getLatitude(), location.getLongitude(), 100, 20);
        earthquakeCall.enqueue(new Callback<Earthquake>() {
            @Override
            public void onResponse(Call<Earthquake> call, Response<Earthquake> response) {
                mEarthquake = response.body();
                Log.v(TAG, "Response: " + mEarthquake.toString());
            }

            @Override
            public void onFailure(Call<Earthquake> call, Throwable t) {
                Log.e(TAG, "Error Response");
                Log.e(TAG, "Request: " + call.request().url().url().toString());
                Log.e(TAG, "Could not get Earthquake Information!");
            }
        });
    }

    private void getForecast(Location location) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.forecast.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VanilaiWeatherService forecastService = retrofit.create(VanilaiWeatherService.class);
        final Resources resources = getResources();
        Call<Forecast> forecastCall = forecastService.getForecast(resources.getString(R.string.forecast_io_app_id), location.getLatitude() + "," + location.getLongitude());
        Toast.makeText(getActivity(), "Request URL: " + forecastCall.request().url().url().toString(), Toast.LENGTH_SHORT).show();
        forecastCall.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                mForecast = response.body();
                Log.v(TAG, "Response: " + mForecast.toString());
                AddressHelper.getAddressFromLocation(mForecast.getLatitude(), mForecast.getLongitude(), getContext(), new GeocoderHandler());
                mTemperature.setText(resources.getString(R.string.current_temperature, mForecast.getCurrentForecast().getTemperature(), "\u00b0"));
                mHumidity.setText(resources.getString(R.string.current_humidity, mForecast.getCurrentForecast().getHumidity()));
                mPrecipitation.setText(resources.getString(R.string.current_precipitation, mForecast.getCurrentForecast().getPrecipitation()));
                mSummary.setText(mForecast.getCurrentForecast().getSummary());
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String curTime = dateFormat.format(new Date(mForecast.getCurrentForecast().getTime()*1000));
                mCurrentTime.setText(curTime);
                mIconImage.setImageResource(getIconId(mForecast.getCurrentForecast().getIcon()));
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                Log.e(TAG, "Error Response");
                Log.e(TAG, "Request: " + call.request().url().url().toString());
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(getActivity(), "Please provide permission to access your current location for appropriate service.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        } else {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLocation == null) {
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            } else {
                double currentLatitude = mLocation.getLatitude();
                double currentLongitude = mLocation.getLongitude();

                Toast.makeText(getActivity(), "OnConnected: " + currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_SHORT).show();

                getForecast(mLocation);
                getEarthquakeInformation(mLocation);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (mLocation == null) {
                        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                    } else {
                        double currentLatitude = mLocation.getLatitude();
                        double currentLongitude = mLocation.getLongitude();

                        Toast.makeText(getActivity(), "OnConnected: " + currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_SHORT).show();

                        getForecast(mLocation);
                        getEarthquakeInformation(mLocation);
                    }
                }
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "In OnLocationChanged");
        getForecast(location);
        getEarthquakeInformation(location);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    static class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            mLocationTextView.setText(locationAddress);
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

    public void setOnTemperatureSwitch() {
        Resources resources = getResources();
        Log.v(TAG, mTemperatureSwitch.getText().toString());
        if(mTemperatureSwitch.getText().toString().equalsIgnoreCase("°F")) {
            mTemperatureSwitch.setChecked(true);
            double currentTemperature = mForecast.getCurrentForecast().getCurrentTemperature();
            int newTemperature = (int) Math.round(((currentTemperature - 32) * 5)/9);
            mTemperature.setText(resources.getString(R.string.current_temperature,
                    newTemperature, "\u00b0"));
            mTemperatureSwitch.setText("°C");
        } else if(mTemperatureSwitch.getText().toString().equalsIgnoreCase("°C")) {
            mTemperatureSwitch.setChecked(false);
            String currentTemperature = mForecast.getCurrentForecast().getTemperature();
            mTemperature.setText(resources.getString(R.string.current_temperature,
                    currentTemperature, "\u00b0"));
            mTemperatureSwitch.setText("°F");
        }
    }


}