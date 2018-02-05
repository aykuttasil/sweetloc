package com.aykuttasil.sweetloc.receiver;

import android.content.Context;
import android.content.Intent;

import com.aykuttasil.sweetloc.service.SingleLocationRequestService;
import com.aykuttasil.sweetloc.util.MyWakefulBroadcastReceiver;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 14.12.2016.
 */

public class SingleLocationRequestReceiver extends MyWakefulBroadcastReceiver {

    @DebugLog
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent_ = new Intent(context, SingleLocationRequestService.class);
        SingleLocationRequestReceiver.startWakefulService(context, intent_);
    }
}
