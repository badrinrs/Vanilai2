package madri.me.vanilai.service;

import madri.me.vanilai.beans.Earthquake;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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
