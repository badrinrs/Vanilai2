package madri.me.vanilai.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by badri on 7/24/16.
 */
public class EarthquakeProperties {
    @SerializedName("properties")
    private EarthquakeInformation mEarthquakeInformation;

    public EarthquakeInformation getEarthquakeInformation() {
        return mEarthquakeInformation;
    }
}
