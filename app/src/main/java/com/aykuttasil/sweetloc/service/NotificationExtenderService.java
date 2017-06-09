package com.aykuttasil.sweetloc.service;

import android.content.Intent;

import com.aykuttasil.sweetloc.app.Const;
import com.aykuttasil.sweetloc.receiver.SingleLocationRequestReceiver;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 14.12.2016.
 */

public class NotificationExtenderService extends com.onesignal.NotificationExtenderService {

    @DebugLog
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {

        OverrideSettings overrideSettings = new OverrideSettings();

        // Gelen notification ı özelleştiriyoruz..
        overrideSettings.extender = builder -> {
            // Sets the background notification color to Green on Android 5.0+ devices.
            //builder.setContentText("Merhaba");
            builder.setColor(new BigInteger("FF00FF00", 16).intValue());
            return builder;
        };

        //OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        //Logger.d("Notification displayed with id: " + displayedResult.androidNotificationId);


        try {
            Logger.json(notification.payload.toJSONObject().toString());
            JSONObject data = notification.payload.additionalData;
            String customKey;
            if (data != null) {
                Logger.json(data.toString());
                String action = data.getString(Const.ACTION);
                Logger.i("Action: " + action);

                switch (action) {
                    case Const.ACTION_KONUM_YOLLA: {
                        Intent singleLocationRequestIntent = new Intent(this, SingleLocationRequestReceiver.class);
                        sendBroadcast(singleLocationRequestIntent);
                        Logger.i("singleLocationRequestIntent Broadcast gönderildi");
                        break;
                    }
                    default: {

                    }
                }

                customKey = data.optString("customkey", null);
                if (customKey != null) {
                    Logger.i("customkey set with value: " + customKey);
                }

            }
        } catch (JSONException e) {
            Logger.e(e, "HATA");
        }

        // Eğer true dönersek default ayarları ezmiş oluruz. displayNotification() çalıştırmazsak bildirim gözükmez.
        return true;
    }
}
