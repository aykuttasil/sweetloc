package com.aykuttasil.sweetloc.service;

import com.aykuttasil.sweetloc.model.event.FcmRegistraionIDEvent;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.i("Refreshed token: " + refreshedToken);

        FcmRegistraionIDEvent fcmRegistraionIDEvent = new FcmRegistraionIDEvent();
        fcmRegistraionIDEvent.setRegID(refreshedToken);

        /*
        ModelSweetLocPreference modelSweetLocPreference = DbManager.getModelSweetLocPreference();
        if (modelSweetLocPreference == null) {
            modelSweetLocPreference = new ModelSweetLocPreference();
        }
        modelSweetLocPreference.setRegId(refreshedToken);
        modelSweetLocPreference.save();
        */

        EventBus.getDefault().post(fcmRegistraionIDEvent);
    }
}