package madri.me.vanilai.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by badri on 7/30/16.
 */
public class AddressComponent {
    @SerializedName("long_name")
    private String mLongName;
    @SerializedName("short_name")
    private String mShortName;
    @SerializedName("types")
    private List<String> addressTypes;

    public List<String> getAddressTypes() {
        return addressTypes;
    }

    public String getLongName() {
        return mLongName;
    }

    public String getShortName() {
        return mShortName;
    }


}
