package madri.me.vanilai.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by badri on 7/23/16.
 */
public class HourlyDetailForecast {
    @SerializedName("time")
    private long mTime;
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("temperature")
    private double mTemperature;
    @SerializedName("apparentTemperature")
    private double mApparentTemperature;
    @SerializedName("icon")
    private String mIcon;

    public String getIcon() {
        return mIcon;
    }

    public long getTime() {
        return mTime;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getTemperature() {
        return Integer.toString((int) Math.round(mTemperature));
    }

    public int getApparentTemperature() {
        return (int) Math.round(mApparentTemperature);
    }
}
