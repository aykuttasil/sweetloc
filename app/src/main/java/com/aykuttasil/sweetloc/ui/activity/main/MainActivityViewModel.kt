package com.aykuttasil.sweetloc.ui.activity.main

import android.app.Application
import android.location.Location
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.aykuttasil.sweetloc.data.repository.LocationRepository
import com.aykuttasil.sweetloc.data.repository.RoomRepository
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.data.toLocationEntity
import com.aykuttasil.sweetloc.helper.SweetLocHelper
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.rx2.await
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
                userRepository.getUserEntitySuspend()?.let { user ->
                    val roomList = roomRepository.getUserRooms(user.userId).await()
                    locationRepository.addUserAndRoomLocation(user, it.toLocationEntity(), roomList).await()
                }
            }
        }
    }

    fun checkUser(): Boolean = runBlocking {
        userRepository.checkUser()
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