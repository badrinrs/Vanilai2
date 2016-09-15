package me.madri.vanilai.service;

import me.madri.vanilai.beans.Forecast;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by badri on 7/23/16.
 */
public interface VanilaiWeatherService {
    @GET("forecast/{appId}/{latlon}")
    Call<Forecast> getForecast(@Path("appId") String appId, @Path("latlon") String latLon);
}
