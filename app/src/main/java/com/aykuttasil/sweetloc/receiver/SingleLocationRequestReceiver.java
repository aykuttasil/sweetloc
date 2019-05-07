package com.aykuttasil.sweetloc.receiver;

import android.content.Context;
import android.content.Intent;

import com.aykuttasil.sweetloc.service.SingleLocationRequestService;

public class SingleLocationRequestReceiver extends MyWakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent_ = new Intent(context, SingleLocationRequestService.class);
        SingleLocationRequestReceiver.startWakefulService(context, intent_);
    }
}
