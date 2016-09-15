package me.madri.vanilai.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bnara on 7/30/2016.
 */
public class GeoLocation {
    @SerializedName("lat")
    private double mLatitude;
    @SerializedName("lng")
    private double mLongitude;

    public double getLongitude() {
        return mLongitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

}
