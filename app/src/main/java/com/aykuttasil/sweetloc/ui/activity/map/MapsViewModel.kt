package com.aykuttasil.sweetloc.ui.activity.map

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aykuttasil.androidbasichelperlib.SuperHelper
import com.aykuttasil.sweetloc.data.local.entity.LocationEntity
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.data.repository.LocationRepository
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.orhanobut.logger.Logger
import com.patloew.rxlocation.RxLocation
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MapsViewModel @Inject constructor(
        private val rxLocation: RxLocation,
        private val userRepository: UserRepository,
        private val locationRepository: LocationRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val sentMyLocation: MutableLiveData<LocationEntity> = MutableLiveData()

    @SuppressLint("MissingPermission")
    fun sendMyLocation(): LiveData<LocationEntity> {
        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(30000)
                .setFastestInterval(5000)

        val locationSettingsRequest = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)
                .build()

        compositeDisposable.add(Observable.combineLatest(
                rxLocation.settings()
                        .checkAndHandleResolution(locationSettingsRequest)
                        .flatMapObservable { granted ->
                            return@flatMapObservable if (granted) {
                                rxLocation.settings()
                                        .checkAndHandleResolution(locationSettingsRequest)
                                        .flatMapObservable { rxLocation.location().updates(locationRequest) }
                            } else {
                                return@flatMapObservable Observable.error<Throwable>(
                                        Exception("Konum izni gerekli."))
                            }
                        },
                userRepository.getUser().toObservable(),
                BiFunction<Location, UserEntity?, LocModel> { loc, user ->
                    val locationEntity = LocationEntity(
                            latitude = loc.latitude,
                            longitude = loc.longitude,
                            accuracy = loc.accuracy.toDouble(),
                            address = loc.provider,
                            time = loc.time,
                            formatTime = SuperHelper.getFormatTime(loc.time),
                            createDate = SuperHelper.getFormatTime())

                    sentMyLocation.value = locationEntity

                    return@BiFunction LocModel(user.userUUID, locationEntity)
                })
                .retry()
                .flatMapCompletable {
                    locationRepository.addLocation(it.userId, it.locationEntity)
                }
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Logger.i("Location gönderildi.")
                }, {
                    it.printStackTrace()
                    Logger.e(it, "HATA")
                }))
        return sentMyLocation
    }

    fun getMyTrackerLocation() {
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    data class LocModel(
            var userId: String,
            var locationEntity: LocationEntity
    )
}