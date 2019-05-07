package com.aykuttasil.sweetloc.service

/*
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
*/