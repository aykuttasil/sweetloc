package com.aykuttasil.sweetloc.data.remote.model

import android.location.Location

/**
 * Created by aykutasil on 23.01.2018.
 */
data class UserData(var locations: Map<String, Location>? = null)