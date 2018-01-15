package com.aykuttasil.sweetloc.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.aykuttasil.sweetloc.app.App;
import com.aykuttasil.sweetloc.receiver.SingleLocationRequestReceiver;
import com.google.android.gms.location.LocationRequest;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import hugo.weaving.DebugLog;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.aykuttasil.sweetloc.helper.SuperHelper.sendLocationInformation;

/**
 * Created by aykutasil on 8.12.2016.
 */

public class SingleLocationRequestService extends IntentService {


    // Konum belirlenip yollanabilmesi için max beklenecek zaman
    private static long LOCATION_TIMEOUT_IN_SECONDS = 45;
    Disposable mDisposable;

    @DebugLog
    public SingleLocationRequestService() {
        super(SingleLocationRequestService.class.getName());
    }

    @DebugLog
    @Override
    protected void onHandleIntent(Intent intent) {

        if (mDisposable != null && !mDisposable.isDisposed()) {
            Logger.i("RxLocation disposed");
            mDisposable.dispose();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Logger.i("Permission is not granted");
            return;
        }


        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(TimeUnit.SECONDS.toMillis(10))
                .setFastestInterval(TimeUnit.SECONDS.toMillis(5))
                //.setNumUpdates(1) // Sadece belirttiğimiz miktar kadar (1) location güncellemesi alır
                //.setExpirationTime() // gerçek zaman vererek location güncellemesi kontrolü yapılabilir. (gereksiz)
                //.setMaxWaitTime() // her bir location güncellemesi için max bekleme süresini belirtebiliriz. setInterval ile ilişkilidir. dikkat et.
                .setExpirationDuration(TimeUnit.SECONDS.toMillis(LOCATION_TIMEOUT_IN_SECONDS)); // Belirttiğimiz süre kadar location güncellemesi alır

        mDisposable = ((App) getApplication()).getRxLocation().location()
                .updates(locationRequest)
                .filter(location -> {
                    Logger.i("Accuracy: " + location.getAccuracy());
                    return location.getAccuracy() < 150;
                })
                .timeout(LOCATION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS, Observable.error(new Exception("Konum sapması çok yüksek.")))
                .subscribe(location -> {
                    sendLocationInformation(location);
                    mDisposable.dispose();
                    SingleLocationRequestReceiver.completeWakefulIntent(intent);
                }, error -> {
                    mDisposable.dispose();
                    Logger.e(error, "HATA");
                });

    }

    @DebugLog
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
