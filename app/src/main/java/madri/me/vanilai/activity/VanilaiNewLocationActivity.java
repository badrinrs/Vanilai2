package madri.me.vanilai.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
 * Created by badri on 9/10/16.
 */
public class VanilaiNewLocationActivity extends AppCompatActivity  {

    private static final String TAG = VanilaiNewLocationActivity.class.getSimpleName();

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
    private RelativeLayout mFullScreenRelativeLayout;

    private double mLatitude;

    private double mLongitude;

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
            MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(VanilaiNewLocationActivity.this)
                    .title("Warning")
                    .icon(ContextCompat.getDrawable(VanilaiNewLocationActivity.this, R.drawable.ic_warning_white_24dp))
                    .content("Network Connection Unavailable. It may be difficult to get location.")
                    .autoDismiss(true).positiveText("Ok").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    });
            dialogBuilder.show();
        }


        Intent locationIntent = getIntent();
        mLatitude = locationIntent.getDoubleExtra("latitude", 0.00);
        mLongitude = locationIntent.getDoubleExtra("longitude", 0.00);

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
        mFullScreenRelativeLayout = (RelativeLayout) findViewById(R.id.fullScreenRelativeLayout);
        mTemperatureSwitch.setText("\u00b0F");

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
                    forecastIntent.putExtra("background", getBackgroundId(mForecast.getCurrentForecast().getIcon()));
                    forecastIntent.putExtra("forecast", target);
                    forecastIntent.putExtra("city", mLocationTextView.getText().toString());
                    forecastIntent.putExtra("forecastType", "weekly");
                    startActivity(forecastIntent);
                } else {
                    Log.e(TAG, "Weekly Forecasts not obtained Yet! Please try again later!");
                    Toast.makeText(getApplicationContext(), "Weekly Forecasts not obtained Yet! Please try again later!", Toast.LENGTH_SHORT).show();
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
                    forecastIntent.putExtra("background", getBackgroundId(mForecast.getCurrentForecast().getIcon()));
                    forecastIntent.putExtra("forecast", target);
                    forecastIntent.putExtra("city", mLocationTextView.getText().toString());
                    forecastIntent.putExtra("forecastType", "hourly");
                    startActivity(forecastIntent);
                } else {
                    Log.e(TAG, "Hourly Forecasts not obtained Yet! Please try again later!");
                    Toast.makeText(getApplicationContext(), "Hourly Forecasts not obtained Yet! Please try again later!", Toast.LENGTH_SHORT).show();
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
                    earthquakeIntent.putExtra("background", getBackgroundId(mForecast.getCurrentForecast().getIcon()));
                    earthquakeIntent.putExtra("earthquake", target);
                    earthquakeIntent.putExtra("city", mLocationTextView.getText().toString());
                    startActivity(earthquakeIntent);
                } else {
                    Toast.makeText(VanilaiNewLocationActivity.this, "Earthquake response Unavailable! Please try again later!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Earthquake response Unavailable! Please try again later!");
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addLocationIntent = new Intent(getApplicationContext(), AddLocationActivity.class);
                startActivity(addLocationIntent);

            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getForecast(mLatitude, mLongitude);
                getEarthquakeInformation(mLatitude, mLongitude);
            }
        });

        ImageButton currentLocationButton = (ImageButton) findViewById(R.id.settingsButton);
        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VanilaiFullScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        getForecast(mLatitude, mLongitude);
        getEarthquakeInformation(mLatitude, mLongitude);
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

    private void getEarthquakeInformation(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://earthquake.usgs.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EarthquakeService earthquakeService = retrofit.create(EarthquakeService.class);
        Call<Earthquake> earthquakeCall = earthquakeService.getEarthquake("geojson", latitude, longitude, 1000, 20);
        earthquakeCall.enqueue(new Callback<Earthquake>() {
            @Override
            public void onResponse(Call<Earthquake> call, Response<Earthquake> response) {
                mEarthquake = response.body();
            }

            @Override
            public void onFailure(Call<Earthquake> call, Throwable t) {
                Toast.makeText(VanilaiNewLocationActivity.this, "Failed to get earthquake information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getForecast(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.forecast.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VanilaiWeatherService forecastService = retrofit.create(VanilaiWeatherService.class);
        final Resources resources = getResources();
        Call<Forecast> forecastCall = forecastService.getForecast(resources.getString(R.string.forecast_io_app_id), latitude + "," + longitude);
        forecastCall.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                mForecast = response.body();
                AddressHelper.getAddressFromLocation(mForecast.getLatitude(), mForecast.getLongitude(), getApplicationContext(), new GeocoderHandler());
                mTemperature.setText(resources.getString(R.string.current_temperature, mForecast.getCurrentForecast().getTemperature(), "\u00b0"));
                mHumidity.setText(resources.getString(R.string.current_humidity, mForecast.getCurrentForecast().getHumidity()));
                mPrecipitation.setText(resources.getString(R.string.current_precipitation, mForecast.getCurrentForecast().getPrecipitation()));
                mSummary.setText(mForecast.getCurrentForecast().getSummary());
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String curTime = dateFormat.format(new Date(mForecast.getCurrentForecast().getTime()*1000));
                mCurrentTime.setText(curTime);
                mIconImage.setImageResource(getIconId(mForecast.getCurrentForecast().getIcon()));
                mFullScreenRelativeLayout.setBackgroundResource(getBackgroundId(mForecast.getCurrentForecast().getIcon()));
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                Toast.makeText(VanilaiNewLocationActivity.this, "Failed to get forecast information.", Toast.LENGTH_SHORT).show();
            }
        });
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

    public static int getBackgroundId(String iconString) {
        int iconId = R.drawable.clear;
        if(iconString.equalsIgnoreCase("clear-day")) {
            iconId = R.drawable.clear;
        } else if(iconString.equalsIgnoreCase("clear-night")) {
            iconId = R.drawable.clearnight;
        } else if(iconString.equalsIgnoreCase("partly-cloudy-day")) {
            iconId = R.drawable.partlycloudyday;
        } else if(iconString.equalsIgnoreCase("cloudy")) {
            iconId = R.drawable.cloudyday;
        } else if(iconString.equalsIgnoreCase("partly-cloudy-night")) {
            iconId = R.drawable.cloudynight;
        } else if(iconString.equalsIgnoreCase("fog")) {
            iconId = R.drawable.foggy_day;
        } else if(iconString.equalsIgnoreCase("rain")) {
            iconId = R.drawable.rainy_day;
        } else if(iconString.equalsIgnoreCase("sleet")) {
            iconId = R.drawable.sleet_day;
        } else if(iconString.equalsIgnoreCase("snow")) {
            iconId = R.drawable.snowy_day;
        } else if(iconString.equalsIgnoreCase("wind")) {
            iconId = R.drawable.windy_day;
        }
        return iconId;
    }
}
