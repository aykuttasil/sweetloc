package com.aykuttasil.sweetloc.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aykutasil on 3.07.2016.
 */
public class BaseResponse {

    @Expose
    @SerializedName("success")
    private boolean Success;

    @Expose
    @SerializedName("msg")
    private String Msg;

    @Expose
    @SerializedName("status")
    private int Status;


    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
