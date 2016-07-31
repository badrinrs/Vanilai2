package madri.me.vanilai.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import madri.me.vanilai.R;
import madri.me.vanilai.beans.Earthquake;
import madri.me.vanilai.beans.Forecast;
import madri.me.vanilai.helper.AddressHelper;
import madri.me.vanilai.service.EarthquakeService;
import madri.me.vanilai.service.VanilaiWeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VanilaiFullScreenActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = VanilaiFullScreenActivity.class.getSimpleName();

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    private Forecast mForecast;

    private static TextView mLocationTextView;
    private TextView mTemperature;
    private TextView mHumidity;
    private TextView mPrecipitation;
    private TextView mSummary;
    private TextView mCurrentTime;
    private ImageView mIconImage;
    private Switch mTemperatureSwitch;
    private Earthquake mEarthquake;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vanilai_full_screen);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.v(TAG, "Internet Connected!");
        } else {
            Log.e(TAG, "Internet DisConnected");
        }
        mLocationTextView = (TextView) findViewById(R.id.location);
        mTemperature = (TextView) findViewById(R.id.temperature);
        Button earthquakeDetected = (Button) findViewById(R.id.earthquakeDetected);
        mHumidity = (TextView) findViewById(R.id.humidity);
        mPrecipitation = (TextView) findViewById(R.id.precipitation);
        mSummary = (TextView) findViewById(R.id.weatherDescription);
        mCurrentTime = (TextView) findViewById(R.id.currentTime);
        mIconImage = (ImageView) findViewById(R.id.currentWeatherImage);
        ImageButton refresh = (ImageButton) findViewById(R.id.refresh);
        Button hourlyButton = (Button) findViewById(R.id.hourlyForecast);
        Button weeklyButton = (Button) findViewById(R.id.weeklyForecastButton);
        mTemperatureSwitch = (Switch) findViewById(R.id.temperatureSwitch);
        ImageButton addButton = (ImageButton) findViewById(R.id.addButton);
        mTemperatureSwitch.setText("\u00b0F");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
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

        refresh.setOnClickListener(new View.OnClickListener() {
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

        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mForecast!=null) {
                    Intent forecastIntent = new Intent(getApplicationContext(), ForecastActivity.class);
                    Gson gson = new Gson();
                    String target = gson.toJson(mForecast);
                    forecastIntent.putExtra("forecast", target);
                    forecastIntent.putExtra("city", mLocationTextView.getText().toString());
                    forecastIntent.putExtra("forecastType", "weekly");
                    startActivity(forecastIntent);
                } else {
                    Log.e(TAG, "Weekly Forecasts not obtained Yet! Please try again later!");
                    Toast.makeText(getApplicationContext(), "Weekly Forecasts not obtained Yet! Please try again later!", Toast.LENGTH_LONG).show();
                }
            }
        });

        hourlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mForecast!=null) {
                    Intent forecastIntent = new Intent(getApplicationContext(), ForecastActivity.class);
                    Gson gson = new Gson();
                    String target = gson.toJson(mForecast);
                    forecastIntent.putExtra("forecast", target);
                    forecastIntent.putExtra("city", mLocationTextView.getText().toString());
                    forecastIntent.putExtra("forecastType", "hourly");
                    startActivity(forecastIntent);
                } else {
                    Log.e(TAG, "Hourly Forecasts not obtained Yet! Please try again later!");
                    Toast.makeText(getApplicationContext(), "Hourly Forecasts not obtained Yet! Please try again later!", Toast.LENGTH_LONG).show();
                }
            }
        });
        earthquakeDetected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEarthquake!=null) {
                    Intent earthquakeIntent = new Intent(getApplicationContext(), EarthquakeActivity.class);
                    Gson gson = new Gson();
                    String target = gson.toJson(mEarthquake);
                    earthquakeIntent.putExtra("earthquake", target);
                    earthquakeIntent.putExtra("city", mLocationTextView.getText().toString());
                    startActivity(earthquakeIntent);
                } else {
                    Log.e(TAG, "Earthquake response Unavailable! Please try again later!");
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(VanilaiFullScreenActivity.this)
                        .title("Add/Remove Location")
                        .customView(R.layout.add_dialog_layout, true)
                        .positiveText("Add/Remove")
                        .negativeText("Cancel").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View dialogView = dialog.getCustomView();
                        if(dialogView!=null) {
                            EditText cityEditText = (EditText) dialogView.findViewById(R.id.dialog_city);
                            EditText countryEditText = (EditText) dialogView.findViewById(R.id.dialog_state);
                            EditText zipEditText = (EditText) dialogView.findViewById(R.id.dialog_zip);
                            Log.v(TAG, "City: "+cityEditText.getText().toString().isEmpty());
                            Log.v(TAG, "Country: "+countryEditText.getText().toString().isEmpty());
                            Log.v(TAG, "Zip: "+zipEditText.getText().toString().isEmpty());
                            if (cityEditText.getText().toString().isEmpty() && countryEditText.getText().toString().isEmpty() && zipEditText.getText().toString().isEmpty()) {
                                Log.e(TAG, "Please provide atleast one!!!");
                            } else {
                                StringBuilder locationBuilder = new StringBuilder();
                                if (!cityEditText.getText().toString().isEmpty()) {
                                    locationBuilder.append(cityEditText.getText().toString()).append(" ");
                                }
                                if (!countryEditText.getText().toString().isEmpty()) {
                                    locationBuilder.append(countryEditText.getText().toString()).append(" ");
                                }
                                if (!zipEditText.getText().toString().isEmpty()) {
                                    locationBuilder.append(countryEditText.getText().toString());
                                }
                                String location = locationBuilder.toString();
                                AddressHelper.getAddressFromLocation(location, getApplicationContext(), new ReverseGeocoderHandler());
                            }
                        }

                        dialog.dismiss();
                    }
                }).show();

            }
        });

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
        Toast.makeText(this, "Request URL: " + forecastCall.request().url().url().toString(), Toast.LENGTH_SHORT).show();
        forecastCall.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                mForecast = response.body();
                Log.v(TAG, "Response: " + mForecast.toString());
                AddressHelper.getAddressFromLocation(mForecast.getLatitude(), mForecast.getLongitude(), getApplicationContext(), new GeocoderHandler());
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Please provide permission to access your current location for appropriate service.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        } else {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLocation == null) {
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            } else {
                double currentLatitude = mLocation.getLatitude();
                double currentLongitude = mLocation.getLongitude();

                Toast.makeText(this, "OnConnected: " + currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_SHORT).show();

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
                        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                    } else {
                        double currentLatitude = mLocation.getLatitude();
                        double currentLongitude = mLocation.getLongitude();

                        Toast.makeText(this, "OnConnected: " + currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_SHORT).show();

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
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
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

    static class ReverseGeocoderHandler extends Handler {
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
            Log.v(TAG, "Location Address: "+locationAddress);
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
