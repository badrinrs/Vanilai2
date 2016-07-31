package madri.me.vanilai.service;

import madri.me.vanilai.beans.GeocoderAddress;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by badri on 7/30/16.
 */
public interface AddressHelper {
    @GET("maps/api/geocode/json")
    Call<GeocoderAddress> getAddress(@Query("address") String address, @Query("key") String apiKey);
}
