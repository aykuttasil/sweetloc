package com.aykuttasil.sweetloc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aykuttasil.sweetloc.helper.SuperHelper;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 11.07.2016.
 */
public class BootupReceiver extends BroadcastReceiver {

    @DebugLog
    @Override
    public void onReceive(Context context, Intent intent) {

        SuperHelper.startPeriodicTask(context);

    }
}
