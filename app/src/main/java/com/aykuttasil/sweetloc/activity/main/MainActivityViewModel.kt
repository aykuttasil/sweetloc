package com.aykuttasil.sweetloc.activity.main

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.aykuttasil.sweetloc.app.App
import com.aykuttasil.sweetloc.db.DbManager
import com.aykuttasil.sweetloc.helper.SuperHelper
import com.onesignal.OneSignal
import com.orhanobut.logger.Logger
import javax.inject.Inject

/**
 * Created by aykutasil on 15.01.2018.
 */
class MainActivityViewModel @Inject constructor(val app: App) : AndroidViewModel(app) {

     val isUserLogin = MutableLiveData<Boolean>()

    fun checkUserLogin(): LiveData<Boolean> {
        if (SuperHelper.checkUser()) {
            SuperHelper.startPeriodicTask(app)

            if (DbManager.getOneSignalUserId() == null) {
                OneSignal.idsAvailable { userId, registrationId ->
                    Logger.i("OneSignal userId: " + userId)
                    Logger.i("OneSignal regId: " + registrationId)

                    val modelUser = DbManager.getModelUser()
                    modelUser.oneSignalUserId = userId
                    modelUser.save()
                    SuperHelper.updateUser(modelUser)

                    isUserLogin.value = true
                }
            } else {
                isUserLogin.value = true
            }
        } else {
            SuperHelper.logoutUser()
            isUserLogin.value = false
        }
        return isUserLogin
    }

}