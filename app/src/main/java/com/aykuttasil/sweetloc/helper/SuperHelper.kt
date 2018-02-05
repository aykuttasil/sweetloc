package com.aykuttasil.sweetloc.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.aykuttasil.sweetloc.BuildConfig
import com.aykuttasil.sweetloc.app.Const
import com.aykuttasil.sweetloc.data.DataManager
import com.aykuttasil.sweetloc.receiver.SingleLocationRequestReceiver
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.onesignal.OneSignal
import com.orhanobut.logger.Logger
import hugo.weaving.DebugLog
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by aykutasil on 12.07.2016.
 */
class SuperHelper @Inject constructor(private val dataManager: DataManager) : com.aykuttasil.androidbasichelperlib.SuperHelper() {

    @DebugLog
    fun resetSweetLoc(context: Context) {
        async(UI) {
            dataManager.getUser()
                    .flatMapCompletable {
                        dataManager.deleteUser(it)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe {
                        stopPeriodicTask(context)
                        logoutUser()
                    }
        }
    }

    fun checkUser(): Single<Boolean> {
        return Single.create { emitter: SingleEmitter<Boolean> ->
            try {
                async(UI)
                {
                    val userEntity = bg { dataManager.getUserEntity() }.await()
                    val firebaseUser = FirebaseAuth.getInstance().currentUser

                    if (userEntity != null && firebaseUser != null) {
                        emitter.onSuccess(true)
                    } else {
                        emitter.onSuccess(false)
                    }
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }

        /*
        async(UI)
        {
            val userEntity = bg { dataManager.getUserEntity() }.await()
            val firebaseUser = FirebaseAuth.getInstance().currentUser

            return@async Single.just(true)
        }
        */

        /* Single.zip(
                Single.defer { Single.just(dataManager.getUserEntity()) },
                Single.defer { Single.just(FirebaseAuth.getInstance().currentUser) },
                BiFunction<UserEntity?, FirebaseUser?, Boolean> { userEntity, firebaseUser ->
                    Logger.i("userEntity: $userEntity\nfirebaseUser:$firebaseUser")
                    return@BiFunction userEntity != null && firebaseUser != null
                })
                .subscribeOn(Schedulers.io())
                */
        //return FirebaseAuth.getInstance().currentUser != null && dataManager.getUser().blockingGet() != null
    }

    @DebugLog
    fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
    }

    /*
    @DebugLog
    fun updateUser(modelUser: ModelUser) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            FirebaseDatabase.getInstance().reference
                    .child(ModelUser::class.java!!.getSimpleName())
                    .child(user.uid)
                    .setValue(modelUser)
        }
    }

    @DebugLog
    fun sendLocationInformation(sendMyLocation: Location) {
        Logger.i("Location gönderildi: " + sendMyLocation)

        val modelLocation = ModelLocation()
        modelLocation.setLatitude(sendMyLocation.latitude)
        modelLocation.setLongitude(sendMyLocation.longitude)
        modelLocation.setAccuracy(sendMyLocation.accuracy)
        modelLocation.setAddress(sendMyLocation.provider)
        modelLocation.setTime(sendMyLocation.time)
        modelLocation.setFormatTime(com.aykuttasil.androidbasichelperlib.SuperHelper.getFormatTime(sendMyLocation.time))
        modelLocation.setCreateDate(com.aykuttasil.androidbasichelperlib.SuperHelper.getFormatTime())

        FirebaseDatabase.getInstance().reference
                .child(ModelLocation::class.java!!.getSimpleName())
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .push()
                .setValue(modelLocation)
    }
*/
    @DebugLog
    fun startPeriodicTask(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context.applicationContext, SingleLocationRequestReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,
                Const.REQUEST_CODE_BROADCAST_LOCATION,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 3000,
                AlarmManager.INTERVAL_HALF_HOUR,
                pendingIntent)
    }


    @DebugLog
    fun stopPeriodicTask(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context.applicationContext, SingleLocationRequestReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,
                Const.REQUEST_CODE_BROADCAST_LOCATION,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.cancel(pendingIntent)
    }

/*
    @DebugLog
    fun CrashlyticsError(error: Throwable) {
        if (DbManager.getModelUser() != null && DbManager.getModelUser().getEmail() != null) {
            Crashlytics.setUserEmail(DbManager.getModelUser().getEmail())
            Crashlytics.logException(error)
        } else {
            Crashlytics.logException(error)
        }
    }

    @DebugLog
    fun CrashlyticsLog(log: String) {
        if (DbManager.getModelUser() != null && DbManager.getModelUser().getEmail() != null) {
            Crashlytics.setUserEmail(DbManager.getModelUser().getEmail())
            Crashlytics.log(log)
        } else {
            Crashlytics.log(log)
        }
    }
    */


    companion object {
        fun sendNotif(action: String, dataManager: DataManager) {
            try {
                async(UI)
                {
                    val mainObject = JSONObject()

                    val contents = JSONObject()
                    contents.put("en", "SweetLoc - Hello")
                    contents.put("tr", "SweetLoc - Merhaba")
                    mainObject.put("contents", contents)

                    val headings = JSONObject()
                    headings.put("en", "SweetLoc - Title")
                    headings.put("tr", "SweetLoc - Başlık")
                    mainObject.put("headings", headings)

                    val data = JSONObject()
                    data.put(Const.ACTION, action)
                    mainObject.put("data", data)

                    val playerIds = JSONArray()

                    val userEntity = bg { dataManager.getUserEntity() }.await()
                    val userTrackerList = bg { dataManager.getUserTrackers(userEntity?.userUUID!!).blockingSingle() }.await()

                    userTrackerList
                            .filter { it.oneSignalUserId != null }
                            .forEach { playerIds.put(it.oneSignalUserId) }

                    if (BuildConfig.DEBUG) {
                        //playerIds.put("428ef398-76d3-4ca9-ab4c-60d591879365");
                        //playerIds.put("cebff33f-4274-49d1-b8ee-b1126325e169");
                    }

                    mainObject.put("include_player_ids", playerIds)
                    Logger.json(mainObject.toString())
                    if (playerIds.length() > 0) {
                        OneSignal.postNotification(mainObject, object : OneSignal.PostNotificationResponseHandler {
                            override fun onSuccess(response: JSONObject) {
                                Logger.json(response.toString())
                            }

                            override fun onFailure(response: JSONObject) {
                                Logger.json(response.toString())
                            }
                        })
                    }
                }
            } catch (e: Exception) {
                Logger.e(e, "HATA")
            }
        }
    }


}
