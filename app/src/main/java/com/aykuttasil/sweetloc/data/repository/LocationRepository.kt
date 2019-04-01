package com.aykuttasil.sweetloc.data.repository

import com.aykuttasil.sweetloc.data.LocationEntity
import com.aykuttasil.sweetloc.data.userLocationsNode
import com.google.firebase.database.DatabaseReference
import io.reactivex.Completable
import javax.inject.Inject


class LocationRepository @Inject constructor(
        private val databaseReference: DatabaseReference
) {

    fun addLocation(userId: String, locationEntity: LocationEntity): Completable {
        return Completable.create { emitter ->
            databaseReference.child(userLocationsNode(userId))
                    .push()
                    .setValue(locationEntity)
                    .addOnSuccessListener {
                        emitter.onComplete()
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
        }
    }
}