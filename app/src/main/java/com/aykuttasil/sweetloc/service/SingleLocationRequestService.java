package com.aykuttasil.sweetloc.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.aykuttasil.sweetloc.receiver.SingleLocationRequestReceiver;
import com.google.android.gms.location.LocationRequest;
import com.orhanobut.logger.Logger;
import com.patloew.rxlocation.RxLocation;

import hugo.weaving.DebugLog;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import static com.aykuttasil.sweetloc.helper.SuperHelper.sendLocationInformation;

/**
 * Created by aykutasil on 8.12.2016.
 */

public class SingleLocationRequestService extends IntentService {

    Disposable mDisposable;

    @DebugLog
    public SingleLocationRequestService() {
        super(SingleLocationRequestService.class.getName());
    }

    @DebugLog
    @Override
    protected void onHandleIntent(Intent intent) {

        RxLocation rxLocation = new RxLocation(this);

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(50000)
                .setFastestInterval(5000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Logger.i("Permission is not granted");
            return;
        }

        mDisposable = rxLocation.location()
                .updates(locationRequest)
                .flatMap(new Function<Location, ObservableSource<Location>>() {
                    @DebugLog
                    @Override
                    public ObservableSource<Location> apply(Location location) throws Exception {
                        if (location.getAccuracy() < 200) {
                            return Observable.just(location);
                        } else {
                            return Observable.error(new Exception("Accuracy > 300"));
                        }
                    }
                })
                .retry()
                .subscribe(location -> {

                    sendLocationInformation(location);

                    mDisposable.dispose();

                    SingleLocationRequestReceiver.completeWakefulIntent(intent);

                }, error -> {
                    Logger.e(error, "HATA");
                });
    }



    @DebugLog
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
