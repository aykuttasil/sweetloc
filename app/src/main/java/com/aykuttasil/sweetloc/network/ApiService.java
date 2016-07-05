package com.aykuttasil.sweetloc.network;

import com.aykuttasil.sweetloc.network.model.LocationRequest;
import com.aykuttasil.sweetloc.network.model.LocationResponse;

import hugo.weaving.DebugLog;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by aykutasil on 23.06.2016.
 */
public interface ApiService {

    @DebugLog
    @POST("Location")
    Observable<LocationResponse> Location(LocationRequest request);


}
