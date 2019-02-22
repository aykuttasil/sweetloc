package com.aykuttasil.sweetloc.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by aykutasil on 3.07.2016.
 */
class LocationRequest : BaseRequest() {

    @SerializedName("loc_latitude")
    @Expose
    var latitude: Double? = null

    @SerializedName("loc_longitude")
    @Expose
    private var Longitude: Double = 0.toDouble()

    @SerializedName("loc_accuracy")
    @Expose
    var accuracy: Int = 0

    @SerializedName("loc_time")
    @Expose
    var time: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    var longitude: Double?
        get() = Longitude
        set(longitude) {
            Longitude = longitude!!
        }
}
