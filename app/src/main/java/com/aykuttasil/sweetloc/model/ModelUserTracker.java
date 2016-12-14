package com.aykuttasil.sweetloc.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 8.12.2016.
 */

@Table(name = "UserTracker", id = "_id")
public class ModelUserTracker extends Model {

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

    public String getAd() {
        return Ad;
    }

    public void setAd(String ad) {
        Ad = ad;
    }

    public String getSoyAd() {
        return SoyAd;
    }

    public void setSoyAd(String soyAd) {
        SoyAd = soyAd;
    }

    public String getProfilePictureUrl() {
        return ProfilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        ProfilePictureUrl = profilePictureUrl;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getOneSignalUserId() {
        return OneSignalUserId;
    }

    public void setOneSignalUserId(String oneSignalUserId) {
        OneSignalUserId = oneSignalUserId;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
