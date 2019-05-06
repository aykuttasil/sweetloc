package com.aykuttasil.sweetloc.service

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.app.JobIntentService
import com.aykuttasil.sweetloc.App
import com.aykuttasil.sweetloc.receiver.SingleLocationRequestReceiver
import com.google.android.gms.location.LocationRequest
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class SingleLocationRequestService : JobIntentService() {
    var mDisposable: Disposable? = null

    override fun onHandleWork(intent: Intent) {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            Logger.i("RxLocation disposed")
            mDisposable!!.dispose()
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Logger.i("Permission is not granted")
            return
        }

        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(TimeUnit.SECONDS.toMillis(10))
            .setFastestInterval(TimeUnit.SECONDS.toMillis(5))
            //.setNumUpdates(1) // Sadece belirttiğimiz miktar kadar (1) sendMyLocation güncellemesi alır
            //.setExpirationTime() // gerçek zaman vererek sendMyLocation güncellemesi kontrolü yapılabilir. (gereksiz)
            //.setMaxWaitTime() // her bir sendMyLocation güncellemesi için max bekleme süresini belirtebiliriz. setInterval ile ilişkilidir. dikkat et.
            .setExpirationDuration(TimeUnit.SECONDS.toMillis(LOCATION_TIMEOUT_IN_SECONDS)) // Belirttiğimiz süre kadar sendMyLocation güncellemesi alır

        mDisposable = (application as App).rxLocation.location()
            .updates(locationRequest)
            .filter { location ->
                Logger.i("Accuracy: " + location.accuracy)
                location.accuracy < 150
            }
            .timeout(
                LOCATION_TIMEOUT_IN_SECONDS,
                TimeUnit.SECONDS,
                Observable.error<Location>(Exception("Konum sapması çok yüksek."))
            )
            .subscribe({
                // sendLocationInformation(sendMyLocation)
                mDisposable!!.dispose()
                SingleLocationRequestReceiver.completeWakefulIntent(intent)
            }, { error ->
                mDisposable!!.dispose()
                Logger.e(error, "HATA")
            })

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {

        // Konum belirlenip yollanabilmesi için max beklenecek zaman
        private val LOCATION_TIMEOUT_IN_SECONDS: Long = 45
    }

}
