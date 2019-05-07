package com.aykuttasil.sweetloc.service

import com.aykuttasil.sweetloc.model.event.FcmRegistraionIDEvent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.greenrobot.eventbus.EventBus

class MyFcmListenerService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        super.onNewToken(token)

        val fcmRegistraionIDEvent = FcmRegistraionIDEvent()
        fcmRegistraionIDEvent.regID = token

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

    override fun onMessageReceived(message: RemoteMessage?) {
        val from = message!!.from
        val data = message.data
    }
}
