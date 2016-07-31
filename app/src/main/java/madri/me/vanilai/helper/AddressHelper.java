package madri.me.vanilai.helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by badri on 7/23/16.
 */
public class AddressHelper {
    private static final String TAG = "LocationAddress";

    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        result = address.getLocality();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }

    public static void getAddressFromLocation(final String location, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addresses= geocoder.getFromLocationName(location, 5); // get the found Address Objects
                    for(Address address: addresses) {
                        Log.v(TAG, "Address: "+address.getLongitude());
                    }
                    StringBuilder builder = new StringBuilder(); // A list to save the coordinates if they are available
                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);
                        if(address.hasLatitude() && address.hasLongitude()) {
                            builder.append("Latitude: ").append(address.getLongitude()).append("\n")
                                    .append("Longitude: ").append(address.getLongitude()).append("\n")
                                    .append("City: ").append(address.getLocality()).append("\n")
                                    .append("State: ").append(address.getAdminArea()).append("\n")
                                    .append("Zip: ").append(address.getPostalCode());
                        }
                    }
                    result = builder.toString();
                    Log.v(TAG, "Result: "+result);
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result == null) {
                        result = "Unable to get Address for this Location: "+location;
                    }
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("address", result);
                    message.setData(bundle);
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}
