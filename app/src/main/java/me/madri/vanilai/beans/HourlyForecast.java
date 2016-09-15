package me.madri.vanilai.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by badri on 7/23/16.
 */
public class HourlyForecast {
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("icon")
    private String mIcon;
    @SerializedName("data")
    private List<HourlyDetailForecast> mHourlyDetailForecastList;

    public String getSummary() {
        return mSummary;
    }

    public String getIcon() {
        return mIcon;
    }

    public List<HourlyDetailForecast> getHourlyDetailForecastList() {
        return mHourlyDetailForecastList;
    }

}
