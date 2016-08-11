package com.aykuttasil.sweetloc.service;

import com.aykuttasil.sweetloc.db.DbManager;
import com.aykuttasil.sweetloc.model.ModelSweetLocPreference;
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

    @DebugLog
    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.i("Refreshed token: " + refreshedToken);

        FcmRegistraionIDEvent fcmRegistraionIDEvent = new FcmRegistraionIDEvent();
        fcmRegistraionIDEvent.setRegID(refreshedToken);

        ModelSweetLocPreference modelSweetLocPreference = DbManager.getModelSweetLocPreference();
        if (modelSweetLocPreference == null) {
            modelSweetLocPreference = new ModelSweetLocPreference();
        }
        modelSweetLocPreference.setRegId(refreshedToken);
        modelSweetLocPreference.save();

        EventBus.getDefault().post(fcmRegistraionIDEvent);
    }
}
