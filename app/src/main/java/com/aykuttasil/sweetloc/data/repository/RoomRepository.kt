package com.aykuttasil.sweetloc.data.repository

import com.aykuttasil.sweetloc.data.Room
import com.aykuttasil.sweetloc.data.local.entity.LocationEntity
import com.aykuttasil.sweetloc.data.roomsNode
import com.aykuttasil.sweetloc.data.userLocationsNode
import com.aykuttasil.sweetloc.data.userRoomsNode
import com.google.firebase.database.DatabaseReference
import io.reactivex.Completable
import javax.inject.Inject


class RoomRepository @Inject constructor(
        private val userRepository: UserRepository,
        private val databaseReference: DatabaseReference
) {

    fun createRoom(roomName: String): Completable {
        val room = Room(roomName)
        return addRoom(room)
    }


    fun addRoom(room: Room): Completable {
        return Completable.create { emitter ->
            val record = databaseReference.child(roomsNode()).push()
            record.setValue(room)
                    .addOnSuccessListener {
                        emitter.onComplete()
                    }
                    .addOnFailureListener { e ->
                        if (emitter.isDisposed) {
                            return@addOnFailureListener
                        }
                        emitter.onError(e)
                    }

            //val user = userRepository.getUser1()
            //addUserRoom(user.userUUID)
        }
    }

    fun addUserRoom(userId: String, room: Room): Completable {
        return Completable.create { emitter ->
            val record = databaseReference.child(userRoomsNode(userId)).push()

            record.setValue(room)
                    .addOnSuccessListener {
                        emitter.onComplete()
                    }
                    .addOnFailureListener { e ->
                        if (emitter.isDisposed) {
                            return@addOnFailureListener
                        }
                        emitter.onError(e)
                    }
        }
    }

    fun addLocation(userId: String, location: LocationEntity): Completable {
        return Completable.create { emitter ->
            databaseReference.child(userLocationsNode(userId))
                    .push()
                    .setValue(location)
                    .addOnSuccessListener {
                        emitter.onComplete()
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
        }
    }
}