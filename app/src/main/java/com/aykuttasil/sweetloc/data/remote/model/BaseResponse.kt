package com.aykuttasil.sweetloc.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by aykutasil on 3.07.2016.
 */
open class BaseResponse {

    @Expose
    @SerializedName("success")
    var isSuccess: Boolean = false

    @Expose
    @SerializedName("msg")
    var msg: String? = null

    @Expose
    @SerializedName("status")
    var status: Int = 0
}
