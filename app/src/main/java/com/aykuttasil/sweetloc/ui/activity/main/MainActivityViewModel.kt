package com.aykuttasil.sweetloc.ui.activity.main

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.aykuttasil.sweetloc.app.App
import com.aykuttasil.sweetloc.data.DataManager
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.helper.SuperHelper
import com.onesignal.OneSignal
import com.orhanobut.logger.Logger
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by aykutasil on 15.01.2018.
 */
class MainActivityViewModel @Inject constructor(val app: App) : AndroidViewModel(app) {

    @Inject
    lateinit var dataManager: DataManager

    @Inject
    lateinit var superHelper: SuperHelper

    private val compositeDisposable = CompositeDisposable()

    val isUserLogin = MutableLiveData<Boolean>()

    fun checkUserLogin(): LiveData<Boolean> {
        compositeDisposable.add(superHelper.checkUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) {
                        superHelper.startPeriodicTask(app)
                        isUserLogin.value = true
                    } else {
                        Logger.i("checkUserLogin: false")
                        superHelper.logoutUser()
                        isUserLogin.value = false
                    }
                }, {
                    it.printStackTrace()
                })
        )

        return Transformations.map(isUserLogin) {
            if (it) {
                saveOneSignalId()
                return@map true
            } else {
                return@map false
            }
        }
    }

    private fun saveOneSignalId() {
        compositeDisposable.add(dataManager.getUser()
                .filter { it.userOneSignalId == null }
                .flatMap {
                    Maybe.create<UserEntity> { emitter ->
                        OneSignal.idsAvailable { userId, registrationId ->
                            Logger.i("OneSignal userId: " + userId)
                            Logger.i("OneSignal regId: " + registrationId)

                            it.userOneSignalId = userId
                            dataManager.updateUser(it)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        emitter.onSuccess(it)
                                    }, { e ->
                                        emitter.onError(e)
                                    })
                        }
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Logger.i("saveOneSignalId is success")
                }, {
                    it.printStackTrace()
                }))
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}