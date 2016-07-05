package com.aykuttasil.sweetloc.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by aykutasil on 3.07.2016.
 */
@Table(name = "Location", id = "_id")
public class ModelLocation extends Model {

    /*
    {
            "id": 1,
            "name": "Aykut Asil",
            "address": "Burhaniye mah. Gürgen Sok. No:24",
            "locLatitude": 42,
            "locLongitude": 23,
            "locAccuracy": 123,
            "locTime": "2016-07-03T00:00:00.000Z",
            "createdAt": "2016-07-03T22:51:51.582Z",
            "updatedAt": "2016-07-03T22:51:51.582Z"
    }
    */

    @SerializedName("name")
    @Expose
    private String Name;

    @SerializedName("address")
    @Expose
    private String Address;

    @SerializedName("locLatitude")
    @Expose
    private double Latitude;

    @SerializedName("locLongitude")
    @Expose
    private double Longitude;

    @SerializedName("locAccuracy")
    @Expose
    private double Accuracy;

    @SerializedName("locTime")
    @Expose
    private Date Time;

    @SerializedName("createdAt")
    @Expose
    private Date CreatedAt;

    @SerializedName("updatedAt")
    @Expose
    private Date UpdatedAt;

    public ModelLocation() {
        super();
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(double accuracy) {
        Accuracy = accuracy;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

    public Date getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        UpdatedAt = updatedAt;
    }
}
