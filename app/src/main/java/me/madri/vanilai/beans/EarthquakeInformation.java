package me.madri.vanilai.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by badri on 7/24/16.
 */
public class EarthquakeInformation {
    @SerializedName("mag")
    private double mMagnitude;
    @SerializedName("place")
    private String mPlace;
    @SerializedName("time")
    private long mTime;

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getPlace() {
        return mPlace;
    }

    public long getTime() {
        return mTime;
    }
}
