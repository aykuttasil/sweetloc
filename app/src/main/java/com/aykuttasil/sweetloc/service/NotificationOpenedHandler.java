package com.aykuttasil.sweetloc.service;

import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.orhanobut.logger.Logger;

public class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

  @Override
  public void notificationOpened(OSNotificationOpenResult result) {
    Logger.json(result.toJSONObject().toString());
  }
}
