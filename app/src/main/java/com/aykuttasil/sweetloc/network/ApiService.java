package com.aykuttasil.sweetloc.network;

import com.aykuttasil.sweetloc.network.model.LocationRequest;
import com.aykuttasil.sweetloc.network.model.LocationResponse;

import hugo.weaving.DebugLog;
import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by aykutasil on 23.06.2016.
 */
public interface ApiService {

    @DebugLog
    @POST("Location")
    Observable<LocationResponse> Location(LocationRequest request);
}
