package me.madri.vanilai.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by badri on 7/24/16.
 */
public class Earthquake {
    @SerializedName("features")
    private List<EarthquakeProperties> mEarthquakeProperties;

    public List<EarthquakeProperties> getEarthquakeProperties() {
        return mEarthquakeProperties;
    }
}
