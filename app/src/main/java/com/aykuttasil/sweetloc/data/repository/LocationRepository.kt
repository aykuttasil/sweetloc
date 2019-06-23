package com.aykuttasil.sweetloc.data.repository

import com.aykuttasil.sweetloc.data.LocationEntity
import com.aykuttasil.sweetloc.data.RoomEntity
import com.aykuttasil.sweetloc.data.RoomMemberLocationModel
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.data.roomMemberLocationNode
import com.aykuttasil.sweetloc.data.roomMembersNode
import com.aykuttasil.sweetloc.data.userLocationsNode
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class LocationRepository @Inject constructor(
    private val databaseReference: DatabaseReference
) {

    /*

    fun addUserLocation(userId: String, locationEntity: LocationEntity): Completable {
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

    fun addRoomMemberLocation(roomId: String, roomMemberLocation: RoomMemberLocationModel): Completable {
        return Completable.create { emitter ->
            databaseReference.child(roomMemberLocationNode(roomId, roomMemberLocation.user!!.userId!!))
                .setValue(roomMemberLocation)
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    fun addUserAndRoomLocation(
        userId: String,
        roomId: String,
        locationEntity: LocationEntity,
        roomMemberLocationModel: RoomMemberLocationModel
    ): Completable {
        return Completable.create { emitter ->
            databaseReference.child(userLocationsNode(userId))
                .push()
                .setValue(locationEntity)
                .addOnSuccessListener {
                    databaseReference.child(roomMemberLocationNode(roomId, roomMemberLocationModel.user!!.userId!!))
                        .setValue(roomMemberLocationModel)
                        .addOnSuccessListener {
                            emitter.onComplete()
                        }
                        .addOnFailureListener {
                            emitter.onError(it)
                        }
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }
    */

    fun addUserAndRoomLocation(
        user: UserEntity,
        locationEntity: LocationEntity,
        roomList: List<RoomEntity>? = null
    ): Completable {
        return Completable.create { emitter ->
            databaseReference.child(userLocationsNode(user.userId))
                .push()
                .setValue(locationEntity)
                .addOnSuccessListener {
                    if (roomList != null) {
                        roomList.forEach { room ->
                            databaseReference.child(
                                roomMemberLocationNode(
                                    room.roomId!!,
                                    user.userId
                                )
                            )
                                //.setValue(RoomMemberLocationModel(user.toUserModel(), locationEntity))
                                .setValue(locationEntity)
                                .addOnSuccessListener {
                                    emitter.onComplete()
                                }
                                .addOnFailureListener {
                                    emitter.onError(it)
                                }
                        }

                    } else {
                        emitter.onComplete()
                    }

                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }


    /*
    fun addUserAndRoomLocation(
        userId: String,
        roomId: String,
        locationEntity: LocationEntity,
        roomList: List<RoomMemberLocationModel>?
    ): Completable {
        return Completable.create { emitter ->
            databaseReference.child(userLocationsNode(userId))
                .push()
                .setValue(locationEntity)
                .addOnSuccessListener {
                    if (roomList != null) {
                        roomList.forEach { roomMemberLocationModel ->
                            databaseReference.child(
                                roomMemberLocationNode(
                                    roomId,
                                    roomMemberLocationModel.user!!.userId!!
                                )
                            )
                                .setValue(roomMemberLocationModel)
                                .addOnSuccessListener {
                                    emitter.onComplete()
                                }
                                .addOnFailureListener {
                                    emitter.onError(it)
                                }
                        }

                    } else {
                        emitter.onComplete()
                    }

                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    */

    fun getRoomMembersLocations(roomId: String): Flowable<RoomMemberLocationModel> {
        return Flowable.create<RoomMemberLocationModel>({ emitter ->

            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (emitter.isCancelled) {
                        return
                    }
                    try {
                        val rooms = arrayListOf<RoomMemberLocationModel>()
                        dataSnapshot.children.forEach {
                            val model = it.getValue(RoomMemberLocationModel::class.java)

                            rooms.add(model!!)
                            emitter.onNext(model)
                        }

                        // emitter.onNext(rooms.toList())
                    } catch (e: Exception) {
                        emitter.onError(Exception("Problem in DB", e))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    if (emitter.isCancelled) {
                        return
                    }
                    emitter.onError(databaseError.toException())
                }
            }

            val childReference = databaseReference.child(roomMembersNode(roomId))
            childReference.addValueEventListener(listener)
            emitter.setCancellable { childReference.removeEventListener(listener) }
        }, BackpressureStrategy.LATEST).observeOn(Schedulers.io())
    }
}