package me.madri.vanilai.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by badri on 7/30/16.
 */
public class GeocoderAddress {
    @SerializedName("results")
    private List<AddressResult> mAddressResultsList;

    public List<AddressResult> getAddressResultsList() {
        return mAddressResultsList;
    }
}
