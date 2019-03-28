package com.aykuttasil.sweetloc.ui.activity.main

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.Transformations
import com.aykuttasil.sweetloc.App
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.helper.SweetLocHelper
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
        private val app: App,
        private val userRepository: UserRepository,
        private val sweetLocHelper: SweetLocHelper
) : AndroidViewModel(app), LifecycleObserver {

    private val compositeDisposable = CompositeDisposable()

    val isUserLogin = MutableLiveData<Boolean>()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumeRunTest() {
        println("onResume running")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPauseRunTest() {
        println("onPause running")
    }

    fun checkUserLogin(): LiveData<Boolean> {
        compositeDisposable.add(sweetLocHelper.checkUser()
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
                })
        )

        return Transformations.map(isUserLogin) {
            if (it) {
                // saveOneSignalId()
                return@map true
            } else {
                return@map false
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}