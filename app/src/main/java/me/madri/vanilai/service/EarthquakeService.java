package me.madri.vanilai.service;

import me.madri.vanilai.beans.Earthquake;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by badri on 7/24/16.
 */
public interface EarthquakeService {
    @GET("/fdsnws/event/1/query")
    Call<Earthquake> getEarthquake(@Query("format") String format,
                                   @Query("latitude") double latitude,
                                   @Query("longitude") double longitude,
                                   @Query("maxradiuskm") int maxRadius,
                                   @Query("limit") int limit);

}
