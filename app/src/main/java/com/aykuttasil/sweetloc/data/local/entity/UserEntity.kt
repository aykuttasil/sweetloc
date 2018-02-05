package com.aykuttasil.sweetloc.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.databinding.BaseObservable

/**
 * Created by aykutasil on 24.12.2017.
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
    @SerializedName("Telefon")
    private String Telefon;

    @Column
    @Expose
    @SerializedName("Email")
    private String Email;

    @Column
    @Expose
    @SerializedName("Parola")
    private String Parola;

    @Column
    @Expose
    @SerializedName("RegId")
    private String RegID;

    @Column
    @Expose
    @SerializedName("Token")
    private String Token;

    @Column
    @Expose
    @SerializedName("UUID")
    private String UUID;

    @Column
    @Expose
    @SerializedName("ImageUrl")
    private String ImageUrl;

    @Column
    @Expose
    @SerializedName("OneSignalUserId")
    private String OneSignalUserId;
 */
@Entity(tableName = "User")
data class UserEntity(
        @PrimaryKey var userUUID: String,
        var userEmail: String,
        var userPassword: String,
        var userName: String? = null,
        var userSurname: String? = null,
        var userTel: String? = null,
        var userRegId: String? = null,
        var userToken: String? = null,
        var userImageUrl: String? = null,
        var userOneSignalId: String? = null,
        var userCity: String? = null
) : BaseObservable() {

    /*
    var UserName: String
        @Bindable
        get() {
            return _UserName
        }
        set(value) {
            _UserName = value
            notifyPropertyChanged(BR.userName)
        }


    var UserJob: String?
        @Bindable
        get() {
            return _UserJob
        }
        set(value) {
            _UserJob = value
            notifyPropertyChanged(BR.userJob)
        }
        */

}