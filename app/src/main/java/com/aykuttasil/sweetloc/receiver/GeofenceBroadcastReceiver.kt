/* Author - Aykut Asil(@aykuttasil) */
package com.aykuttasil.sweetloc.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.aykuttasil.sweetloc.service.GeofenceTransitionsJobIntentService

/**
 * Receiver for geofence transition changes.
 * <p>
 * Receives geofence transition events from Location Services in the form of an Intent containing
 * the transition type and geofence id(s) that triggered the transition. Creates a JobIntentService
 * that will handle the intent in the background.
 */
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            // Enqueues a JobIntentService passing the context and intent as parameters
            GeofenceTransitionsJobIntentService.enqueueWork(context, intent)
        }
    }
}
