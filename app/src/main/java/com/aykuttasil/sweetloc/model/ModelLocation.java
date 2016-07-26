package com.aykuttasil.sweetloc.model;

import android.location.Location;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    @Column
    @SerializedName("Name")
    @Expose
    private String Name;

    @Column
    @SerializedName("Address")
    @Expose
    private String Address;

    @Column
    @SerializedName("LocLatitude")
    @Expose
    private double Latitude;

    @Column
    @SerializedName("LocLongitude")
    @Expose
    private double Longitude;

    @Column
    @SerializedName("LocAccuracy")
    @Expose
    private double Accuracy;

    @Column
    @SerializedName("LocTime")
    @Expose
    private long Time;

    @Column
    @SerializedName("LocFormatTime")
    @Expose
    private String FormatTime;

    @Expose
    private Location Location;

    @Expose
    private String CreateDate;

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


    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public String getFormatTime() {
        return FormatTime;
    }

    public void setFormatTime(String formatTime) {
        FormatTime = formatTime;
    }

    public android.location.Location getLocation() {
        return Location;
    }

    public void setLocation(android.location.Location location) {
        Location = location;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }
}
