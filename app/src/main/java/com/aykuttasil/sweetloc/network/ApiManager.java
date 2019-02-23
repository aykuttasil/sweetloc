package com.aykuttasil.sweetloc.network;

import android.annotation.SuppressLint;

import com.aykuttasil.sweetloc.network.model.LocationRequest;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aykutasil on 23.06.2016.
 */
public class ApiManager {

    @SuppressLint("CheckResult")
    public static void Location(LocationRequest request) {
        try {
            RestClient restClient = RestClient.getInstance();
            restClient.getApiService().Location(request)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        //resp.getData();
                    }, error -> {
                        EventBus.getDefault().post("");
                    });
        } catch (Exception e) {
            //
        }
    }


}
