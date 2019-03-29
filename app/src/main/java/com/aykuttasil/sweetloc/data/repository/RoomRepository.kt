package com.aykuttasil.sweetloc.data.repository

import com.aykuttasil.sweetloc.data.Room
import com.aykuttasil.sweetloc.data.local.entity.LocationEntity
import com.aykuttasil.sweetloc.data.roomsNode
import com.aykuttasil.sweetloc.data.userLocationsNode
import com.aykuttasil.sweetloc.data.userRoomNode
import com.google.firebase.database.DatabaseReference
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class RoomRepository @Inject constructor(
        private val userRepository: UserRepository,
        private val databaseReference: DatabaseReference
) {

    fun createRoom(roomName: String): Completable {
        return Completable.create {
            try {
                var a = runBlocking(Dispatchers.IO){
                    val room = Room(roomName)
                    room
                }

                val room = Room(roomName)
                val user = userRepository.getUser1()
                val roomId = addRoom(room).blockingGet()
                addUserRoom(user!!.userUUID, roomId, room)
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }


    /*
    fun addRoom(room: Room): Completable {
        return Completable.create { emitter ->
            val record = databaseReference.child(roomsNode()).push()
            record.setValue(room)
                    .addOnSuccessListener {
                        emitter.onComplete()
                    }.addOnFailureListener { e ->
                        if (emitter.isDisposed) {
                            return@addOnFailureListener
                        }
                        emitter.onError(e)
                    }
        }
    }

     */

    fun addRoom(room: Room): Single<String> {
        return Single.create { emitter ->
            val record = databaseReference.child(roomsNode()).push()
            record.setValue(room)
                    .addOnSuccessListener {
                        emitter.onSuccess(record.key ?: "")
                    }.addOnFailureListener { e ->
                        if (emitter.isDisposed) {
                            return@addOnFailureListener
                        }
                        emitter.onError(e)
                    }
        }
    }

    fun addUserRoom(userId: String, roomId: String, room: Room): Completable {
        return Completable.create { emitter ->
            val record = databaseReference.child(userRoomNode(userId, roomId)).push()

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