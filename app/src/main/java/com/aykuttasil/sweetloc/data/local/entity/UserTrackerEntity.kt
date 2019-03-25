package com.aykuttasil.sweetloc.data.local.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

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
        var name: String? = null,
        var surName: String? = null,
        var email: String?,
        var profilePictureUrl: String? = null,
        var oneSignalUserId: String? = null,
        var token: String? = null,
        var UUID: String? = null
) {

    @Ignore
    constructor() : this(email = "")
}