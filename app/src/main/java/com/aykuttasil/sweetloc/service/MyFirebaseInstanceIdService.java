package com.aykuttasil.sweetloc.service;

import com.aykuttasil.sweetloc.model.event.FcmRegistraionIDEvent;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 20.11.2015.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseInstanceId";

    @DebugLog
    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.i("Refreshed token: " + refreshedToken);

        FcmRegistraionIDEvent fcmRegistraionIDEvent = new FcmRegistraionIDEvent();
        fcmRegistraionIDEvent.setRegID(refreshedToken);

        EventBus.getDefault().post(fcmRegistraionIDEvent);

        // TODO: Implement this method to send any registration to your app's servers.
        //sendRegistrationToServer(refreshedToken);
    }
}
