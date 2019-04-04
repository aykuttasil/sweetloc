package com.aykuttasil.sweetloc.service

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import androidx.core.app.NotificationCompat
import com.aykuttasil.sweetloc.app.Const
import com.aykuttasil.sweetloc.receiver.SingleLocationRequestReceiver
import com.onesignal.OSNotificationReceivedResult
import com.orhanobut.logger.Logger
import org.json.JSONException
import java.math.BigInteger

class NotificationExtenderService : com.onesignal.NotificationExtenderService() {

    override fun onNotificationProcessing(notification: OSNotificationReceivedResult): Boolean {

        val overrideSettings = OverrideSettings()

        // Gelen notification ı özelleştiriyoruz..
        overrideSettings.extender = NotificationCompat.Extender { builder ->
            // Sets the background notification color to Green on Android 5.0+ devices.
            builder.setColor(BigInteger("FF00FF00", 16).toInt())
        }

        //OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        //Logger.d("Notification displayed with id: " + displayedResult.androidNotificationId);


        try {
            Logger.json(notification.payload.toJSONObject().toString())
            val data = notification.payload.additionalData
            val customKey: String?
            if (data != null) {
                Logger.json(data.toString())
                val action = data.getString(Const.ACTION)
                Logger.i("Action: $action")

                when (action) {
                    Const.ACTION_KONUM_YOLLA -> {
                        val singleLocationRequestIntent = Intent(this, SingleLocationRequestReceiver::class.java)
                        sendBroadcast(singleLocationRequestIntent)
                        Logger.i("singleLocationRequestIntent Broadcast gönderildi")
                    }
                    Const.ACTION_PHONE_UNMUTE -> {
                        val audioManager = this.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
                        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                        audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND)
                    }
                    else -> {
                        val displayedResult = displayNotification(overrideSettings)
                        Logger.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId)
                        return true
                    }
                }

                customKey = data.optString("customkey", null)
                if (customKey != null) {
                    Logger.i("customkey set with value: $customKey")
                }

            } else {
                val displayedResult = displayNotification(overrideSettings)
                Logger.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId)
                return true
            }
        } catch (e: JSONException) {
            Logger.e(e, "HATA")
        }

        // Eğer true dönersek default ayarları ezmiş oluruz. displayNotification() çalıştırmazsak bildirim gözükmez.
        return true
    }
}
