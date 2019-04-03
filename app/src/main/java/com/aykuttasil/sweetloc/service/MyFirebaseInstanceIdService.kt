package com.aykuttasil.sweetloc.service

import com.aykuttasil.sweetloc.model.event.FcmRegistraionIDEvent
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.orhanobut.logger.Logger

import org.greenrobot.eventbus.EventBus

class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {

        val refreshedToken = FirebaseInstanceId.getInstance().token
        Logger.i("Refreshed token: " + refreshedToken!!)

        val fcmRegistraionIDEvent = FcmRegistraionIDEvent()
        fcmRegistraionIDEvent.regID = refreshedToken

        /*
        ModelSweetLocPreference modelSweetLocPreference = DbManager.getModelSweetLocPreference();
        if (modelSweetLocPreference == null) {
            modelSweetLocPreference = new ModelSweetLocPreference();
        }
        modelSweetLocPreference.setRegId(refreshedToken);
        modelSweetLocPreference.save();
        */

        EventBus.getDefault().post(fcmRegistraionIDEvent)
    }
}