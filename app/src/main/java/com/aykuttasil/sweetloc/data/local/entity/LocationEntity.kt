package com.aykuttasil.sweetloc.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by aykutasil on 27.12.2017.
 */
/*
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

    @Column
    @SerializedName("LocSpeed")
    @Expose
    private long LocSpeed;

    @Column
    @Expose
    private String CreateDate;
 */
@Entity(tableName = "Location")
data class LocationEntity(
        @PrimaryKey(autoGenerate = true) val locationId: Long? = null,
        val name: String? = null,
        val adress: String? = null,
        val latitude: Double,
        val longitude: Double,
        val accuracy: Double?,
        val time: Long,
        val formatTime: String?,
        val speed: Long? = null,
        val createDate: String?,
        val placesName: String? = null
)