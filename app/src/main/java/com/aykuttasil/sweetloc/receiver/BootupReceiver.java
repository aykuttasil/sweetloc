package com.aykuttasil.sweetloc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aykuttasil.sweetloc.activity.MainActivity_;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 11.07.2016.
 */
public class BootupReceiver extends BroadcastReceiver {
    @DebugLog
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent _intent = new Intent(context, MainActivity_.class);
        _intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(_intent);
    }
}
