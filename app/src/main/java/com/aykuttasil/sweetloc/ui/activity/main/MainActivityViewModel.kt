package com.aykuttasil.sweetloc.ui.activity.main

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.aykuttasil.sweetloc.App
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.helper.SweetLocHelper
import com.aykuttasil.sweetloc.util.BaseAndroidViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
        private val app: App,
        private val userRepository: UserRepository,
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