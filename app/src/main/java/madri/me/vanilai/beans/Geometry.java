package madri.me.vanilai.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by badri on 7/30/16.
 */
public class Geometry {
    @SerializedName("location")
    private GeoLocation mGeoLocation;

    public GeoLocation getGeoLocation() {
        return mGeoLocation;
    }

}
