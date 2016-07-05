package com.aykuttasil.sweetloc.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 3.07.2016.
 */
public class LocationRequest extends BaseRequest {

    @SerializedName("loc_latitude")
    @Expose
    private Double Latitude;

    @SerializedName("loc_longitude")
    @Expose
    private double Longitude;

    @SerializedName("loc_accuracy")
    @Expose
    private int Accuracy;

    @SerializedName("loc_time")
    @Expose
    private String Time;

    @SerializedName("address")
    @Expose
    private String Address;

    @SerializedName("name")
    @Expose
    private String Name;


    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public int getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(int accuracy) {
        Accuracy = accuracy;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
