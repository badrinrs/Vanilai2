package madri.me.vanilai.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by badri on 7/9/16.
 */
public class Forecast {
    @SerializedName("latitude")
    private double mLatitude;
    @SerializedName("longitude")
    private double mLongitude;
    @SerializedName("currently")
    private CurrentForecast mCurrentForecast;
    @SerializedName("hourly")
    private HourlyForecast mHourlyForecast;
    @SerializedName("daily")
    private WeeklyForecast mWeeklyForecast;

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public CurrentForecast getCurrentForecast() {
        return mCurrentForecast;
    }

    public HourlyForecast getHourlyForecast() {
        return mHourlyForecast;
    }

    public WeeklyForecast getWeeklyForecast() {
        return mWeeklyForecast;
    }
}
