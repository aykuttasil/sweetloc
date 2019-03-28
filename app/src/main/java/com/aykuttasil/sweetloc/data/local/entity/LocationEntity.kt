package com.aykuttasil.sweetloc.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Location")
data class LocationEntity(
        @PrimaryKey(autoGenerate = true) val locationId: Long? = null,
        val name: String? = null,
        val address: String? = null,
        val latitude: Double,
        val longitude: Double,
        val accuracy: Double?,
        val time: Long,
        val formatTime: String?,
        val speed: Long? = null,
        val createDate: String?,
        val placesName: String? = null
)