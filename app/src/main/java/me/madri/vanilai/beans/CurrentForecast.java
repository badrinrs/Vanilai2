package me.madri.vanilai.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by badri on 7/9/16.
 */
public class CurrentForecast {
    @SerializedName("time")
    private long mTime;
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("precipProbability")
    private double mPrecipitation;
    @SerializedName("humidity")
    private double mHumidity;
    @SerializedName("temperature")
    private double mTemperature;
    @SerializedName("apparentTemperature")
    private double mFeelsLikeTemperature;
    @SerializedName("icon")
    private String mIcon;

    public long getTime() {
        return mTime;
    }

    public String getSummary() {
        return mSummary;
    }

    public int getPrecipitation() {
        return (int) mPrecipitation*100;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public String getTemperature() {
        return Integer.toString((int) Math.round(mTemperature));
    }

    public double getCurrentTemperature() {
        return mTemperature;
    }

    public String getFeelsLikeTemperature() {
        return Integer.toString((int) Math.round(mFeelsLikeTemperature));
    }

    public String getIcon() {
        return mIcon;
    }
}
