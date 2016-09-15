package me.madri.vanilai.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by badri on 7/24/16.
 */
public class WeeklyDetailForecast {
    @SerializedName("time")
    private long mTime;
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("icon")
    private String mIcon;
    @SerializedName("temperatureMin")
    private double mMinTemperature;
    @SerializedName("temperatureMax")
    private double mMaxTemperature;
    @SerializedName("apparentTemperatureMin")
    private double mApparentMinTemperature;
    @SerializedName("apparentTemperatureMax")
    private double mApparentMaxTemperature;

    public long getTime() {
        return mTime;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getIcon() {
        return mIcon;
    }

    public int getMinTemperature() {
        return (int) Math.round(mMinTemperature);
    }

    public int getMaxTemperature() {
        return (int) Math.round(mMaxTemperature);
    }

    public int getApparentMinTemperature() {
        return (int) Math.round(mApparentMinTemperature);
    }

    public int getApparentMaxTemperature() {
        return (int) Math.round(mApparentMaxTemperature);
    }
}
