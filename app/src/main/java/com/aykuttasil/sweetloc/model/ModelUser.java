package com.aykuttasil.sweetloc.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 4.07.2016.
 */
@Table(name = "User", id = "_id")
public class ModelUser extends Model {

    @Expose
    @SerializedName("Ad")
    private String Ad;

    @Expose
    @SerializedName("Ad")
    private String SoyAd;

    @Expose
    @SerializedName("Ad")
    private String Telefon;

    @Expose
    @SerializedName("Ad")
    private String Email;

    @Expose
    @SerializedName("RegId")
    private String RegID;

    @Expose
    @SerializedName("Token")
    private String Token;

    public ModelUser() {
        super();
    }

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

    public String getTelefon() {
        return Telefon;
    }

    public void setTelefon(String telefon) {
        Telefon = telefon;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getRegID() {
        return RegID;
    }

    public void setRegID(String regID) {
        RegID = regID;
    }
}
