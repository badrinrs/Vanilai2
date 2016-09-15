package me.madri.vanilai.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by badri on 7/24/16.
 */
public class WeeklyForecast {
    @SerializedName("data")
    private List<WeeklyDetailForecast> mWeeklyDetailForecastList;
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("icon")
    private String mIcon;

    public List<WeeklyDetailForecast> getWeeklyDetailForecastList() {
        return mWeeklyDetailForecastList;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getIcon() {
        return mIcon;
    }
}
