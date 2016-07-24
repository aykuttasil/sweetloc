package com.aykuttasil.sweetloc.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.aykuttasil.sweetloc.service.PeriodicService_;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 11.07.2016.
 */
public class LocationReceiver extends WakefulBroadcastReceiver {

    public static final String ACTION_PERIODIC_SERVICE = "ActionPeriodicService";

    @DebugLog
    @Override
    public void onReceive(Context context, Intent intent) {
        //Intent intent = new Intent(ACTION_PERIODIC_SERVICE);
        Intent intent_ = new Intent(context, PeriodicService_.class);
        startWakefulService(context, intent_);
    }
}
