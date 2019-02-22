package com.aykuttasil.sweetloc.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by aykutasil on 4.07.2016.
 */
class MyFcmListenerService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage?) {
        val from = message!!.from
        val data = message.data
    }
}
