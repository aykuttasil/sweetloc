package com.aykuttasil.sweetloc.network.model;

import com.aykuttasil.sweetloc.model.ModelLocation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 3.07.2016.
 */
public class LocationResponse extends BaseResponse {

    @Expose
    @SerializedName("Data")
    private ModelLocation Data;

    public ModelLocation getData() {
        return Data;
    }

    public void setData(ModelLocation data) {
        Data = data;
    }
}
