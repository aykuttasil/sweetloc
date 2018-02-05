package com.aykuttasil.sweetloc.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created by aykutasil on 22.01.2018.
 */
/*
 @Column
    @Expose
    @SerializedName("Ad")
    private String Ad;

    @Column
    @Expose
    @SerializedName("SoyAd")
    private String SoyAd;

    @Column
    @Expose
    @SerializedName("Email")
    private String Email;

    @Column
    @Expose
    @SerializedName("ProfilePictureUrl")
    private String ProfilePictureUrl;

    @Column
    @Expose
    @SerializedName("OneSignalUserId")
    private String OneSignalUserId;

    @Column
    @Expose
    @SerializedName("Token")
    private String Token;

    @Column
    @Expose
    @SerializedName("UUID")
    private String UUID;
 */
@Entity(tableName = "UserTracker")
data class UserTrackerEntity(
        @PrimaryKey(autoGenerate = true) var userTrackerId: Long? = null,
        val name: String? = null,
        val surName: String? = null,
        val email: String?,
        val profilePictureUrl: String? = null,
        val oneSignalUserId: String? = null,
        val token: String? = null,
        val UUID: String? = null
) {

    @Ignore
    constructor() : this(email = "")
}