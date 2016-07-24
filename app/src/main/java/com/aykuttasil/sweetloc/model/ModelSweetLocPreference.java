package com.aykuttasil.sweetloc.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 10.07.2016.
 */
public class ModelSweetLocPreference extends Model {

    @Column
    @SerializedName("RegId")
    @Expose
    private String RegId;


    public ModelSweetLocPreference() {
        super();
    }

    public String getRegId() {
        return RegId;
    }

    public void setRegId(String regId) {
        RegId = regId;
    }
}
