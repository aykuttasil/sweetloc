package com.aykuttasil.sweetloc.service;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 7.12.2016.
 */

public class NotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {

    @DebugLog
    @Override
    public void notificationReceived(OSNotification notification) {

        JSONObject data = notification.payload.additionalData;
        String customKey;

        if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Logger.i("OneSignal", "customkey set with value: " + customKey);
        }
    }
}
