package com.aykuttasil.sweetloc.service;

import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.orhanobut.logger.Logger;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 7.12.2016.
 */

public class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

    @DebugLog
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        Logger.json(result.toJSONObject().toString());
    }
}
