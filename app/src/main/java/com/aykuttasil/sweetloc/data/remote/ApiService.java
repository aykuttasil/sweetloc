package com.aykuttasil.sweetloc.data.remote;

import com.aykuttasil.sweetloc.data.remote.model.LocationRequest;
import com.aykuttasil.sweetloc.data.remote.model.LocationResponse;

import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by aykutasil on 23.06.2016.
 */
public interface ApiService {

    @POST("Location")
    Observable<LocationResponse> Location(LocationRequest request);
}
