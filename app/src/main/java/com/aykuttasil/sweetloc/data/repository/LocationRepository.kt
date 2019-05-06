package com.aykuttasil.sweetloc.data.repository

import com.aykuttasil.sweetloc.data.LocationEntity
import com.aykuttasil.sweetloc.data.RoomLocationModel
import com.aykuttasil.sweetloc.data.roomLocationsNode
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

    fun addRoomLocation(roomId: String, roomLocation: RoomLocationModel): Completable {
        return Completable.create { emitter ->
            databaseReference.child(roomLocationsNode(roomId))
                .push()
                .setValue(roomLocation)
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }
}