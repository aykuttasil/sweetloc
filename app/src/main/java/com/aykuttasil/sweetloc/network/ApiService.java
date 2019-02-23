package com.aykuttasil.sweetloc.network;

import com.aykuttasil.sweetloc.network.model.LocationRequest;
import com.aykuttasil.sweetloc.network.model.LocationResponse;

import io.reactivex.Observable;
import retrofit2.http.POST;

public interface ApiService {

    @POST("Location")
    Observable<LocationResponse> Location(LocationRequest request);
}
