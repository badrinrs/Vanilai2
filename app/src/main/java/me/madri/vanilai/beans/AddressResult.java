package me.madri.vanilai.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by badri on 7/30/16.
 */
public class AddressResult {
    @SerializedName("address_components")
    private List<AddressComponent> mAddressComponentList;
    @SerializedName("geometry")
    private Geometry mGeometry;

    public List<AddressComponent> getAddressComponentList() {
        return mAddressComponentList;
    }

    public Geometry getGeometry() {
        return mGeometry;
    }
}
