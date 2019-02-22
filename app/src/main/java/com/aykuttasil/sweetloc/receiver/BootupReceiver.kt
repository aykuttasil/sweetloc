package com.aykuttasil.sweetloc.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import hugo.weaving.DebugLog

/**
 * Created by aykutasil on 11.07.2016.
 */
class BootupReceiver : BroadcastReceiver() {

    @DebugLog
    override fun onReceive(context: Context, intent: Intent) {
        //SweetLocHelper.startPeriodicTask(context)
    }
}
