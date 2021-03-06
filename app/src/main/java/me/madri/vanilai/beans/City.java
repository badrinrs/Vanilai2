package me.madri.vanilai.beans;

/**
 * Created by badri on 7/27/16.
 */
public class City {
    private long mId;
    private String mCity;
    private String mZip;
    private String mState;
    private String mCountry;
    private double mLatitude;
    private double mLongitude;
    private double mTemperature;


    public long getId() {
        return mId;
    }

    public String getCity() {
        return mCity;
    }

    public String getZip() {
        return mZip;
    }

    public String getState() {
        return mState;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public void setZip(String zip) {
        mZip = zip;
    }

    public void setState(String state) {
        mState = state;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

}
