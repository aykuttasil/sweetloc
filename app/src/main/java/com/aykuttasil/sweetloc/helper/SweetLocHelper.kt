/* Author - Aykut Asil(@aykuttasil) */
package com.aykuttasil.sweetloc.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.aykuttasil.androidbasichelperlib.SuperHelper
import com.aykuttasil.sweetloc.BuildConfig
import com.aykuttasil.sweetloc.app.Const
import com.aykuttasil.sweetloc.data.repository.RoomRepository
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.receiver.SingleLocationRequestReceiver
import com.google.firebase.auth.FirebaseAuth
import com.onesignal.OneSignal
import com.orhanobut.logger.Logger
import io.reactivex.Single
import io.reactivex.SingleEmitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.rx2.await
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class SweetLocHelper @Inject constructor(
  private val userRepository: UserRepository,
  private val roomRepository: RoomRepository
) : SuperHelper() {

    fun resetSweetLoc(context: Context) = runBlocking(context = Dispatchers.IO) {
        val user = userRepository.getUser().await()
        user?.apply {
            userRepository.deleteUserFromLocal(this).await()
        }

        stopPeriodicTask(context)
        userRepository.logoutUser()

        /*
        userRepository.getUser()
                .flatMapCompletable {
                    userRepository.deleteUserFromLocal(it)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    stopPeriodicTask(context)
                    logoutUser()
                }
         */
    }

    fun checkUser(): Single<Boolean> {
        return Single.create { emitter: SingleEmitter<Boolean> ->
            runBlocking {
                val userEntity = userRepository.getUserEntitySuspend()
                val firebaseUser = FirebaseAuth.getInstance().currentUser

                if (userEntity != null && firebaseUser != null) {
                    emitter.onSuccess(true)
                } else {
                    emitter.onSuccess(false)
                }
            }
            /*
            try {
                val q = GlobalScope.async(context = Dispatchers.Main) {
                    val userEntity = withContext(Dispatchers.Default) { userRepository.getUserEntity() }
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
            */
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
        // return FirebaseAuth.getInstance().currentUser != null && dataManager.getUser().blockingGet() != null
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
        modelLocation.setFormatTime(com.aykuttasil.androidbasichelperlib.SweetLocHelper.getFormatTime(sendMyLocation.time))
        modelLocation.setCreateDate(com.aykuttasil.androidbasichelperlib.SweetLocHelper.getFormatTime())

        FirebaseDatabase.getInstance().reference
                .child(ModelLocation::class.java!!.getSimpleName())
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .push()
                .setValue(modelLocation)
    }
    */

    fun startPeriodicTask(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context.applicationContext, SingleLocationRequestReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Const.REQUEST_CODE_BROADCAST_LOCATION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 3000,
            AlarmManager.INTERVAL_HALF_HOUR,
            pendingIntent
        )
    }

    fun stopPeriodicTask(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context.applicationContext, SingleLocationRequestReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Const.REQUEST_CODE_BROADCAST_LOCATION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

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
        fun sendNotif(
          action: String,
          userRepository: UserRepository,
          roomRepository: RoomRepository
        ) {
            try {
                runBlocking {
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

                    /*
                    val userEntity = userRepository.getUserEntity()
                    val userTrackerList = withContext(Dispatchers.IO) {
                        userTrackerRepository.getTrackerList(userEntity?.userId!!).blockingSingle()
                    }


                    userTrackerList
                            .filter { it.oneSignalUserId != null }
                            .foreach {
                                playerIds.put(it.oneSignalUserId)
                            }

                     */

                    if (BuildConfig.DEBUG) {
                        // playerIds.put("428ef398-76d3-4ca9-ab4c-60d591879365");
                        // playerIds.put("cebff33f-4274-49d1-b8ee-b1126325e169");
                    }

                    mainObject.put("include_player_ids", playerIds)
                    Logger.json(mainObject.toString())
                    if (playerIds.length() > 0) {
                        OneSignal.postNotification(mainObject,
                            object : OneSignal.PostNotificationResponseHandler {
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
