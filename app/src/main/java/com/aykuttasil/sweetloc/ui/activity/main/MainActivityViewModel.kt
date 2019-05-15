package com.aykuttasil.sweetloc.ui.activity.main

import android.app.Application
import android.location.Location
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.aykuttasil.sweetloc.data.LocationEntity
import com.aykuttasil.sweetloc.data.RoomLocationModel
import com.aykuttasil.sweetloc.data.repository.LocationRepository
import com.aykuttasil.sweetloc.data.repository.RoomRepository
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.helper.SweetLocHelper
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val app: Application,
    private val userRepository: UserRepository,
    private val locationRepository: LocationRepository,
    private val roomRepository: RoomRepository,
    private val sweetLocHelper: SweetLocHelper
) : BaseAndroidViewModel(app), LifecycleObserver {

    val isUserLogin = MutableLiveData<Boolean>()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumeRunTest() {
        println("onResume running")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPauseRunTest() {
        println("onPause running")
    }

    fun updateLocation(location: Location?) {
        launch {
            location?.let {
                val locationEntity = LocationEntity().apply {
                    latitude = it.latitude
                    longitude = it.longitude
                    accuracy = it.accuracy
                    time = it.time
                }
                val user = userRepository.getUserEntity()
                user?.let {
                    withContext(Dispatchers.Default) {
                        locationRepository.addLocation(user.userId, locationEntity).await()

                        val roomLocation = RoomLocationModel(user, locationEntity)
                        val roomList = roomRepository.getUserRooms(user.userId).await()
                        for (room in roomList) {
                            locationRepository.addRoomLocation(room.roomId!!, roomLocation).await()
                        }
                    }
                }
            }
        }
    }


    /*
    fun checkUserLogin(): LiveData<Boolean> {
        sweetLocHelper.checkUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) {
                        sweetLocHelper.startPeriodicTask(app)
                        isUserLogin.value = true
                    } else {
                        Logger.i("checkUserLogin: false")
                        sweetLocHelper.logoutUser()
                        isUserLogin.value = false
                    }
                }, {
                    it.printStackTrace()
                }).addTo(disposables)

        return Transformations.map(isUserLogin) {
            if (it) {
                // saveOneSignalId()
                return@map true
            } else {
                return@map false
            }
        }
    }

     */
}