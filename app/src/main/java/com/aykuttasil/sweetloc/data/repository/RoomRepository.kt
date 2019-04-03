package com.aykuttasil.sweetloc.data.repository

import com.aykuttasil.sweetloc.data.RoomEntity
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.data.roomMembersNode
import com.aykuttasil.sweetloc.data.roomsNode
import com.aykuttasil.sweetloc.data.userRoomNode
import com.aykuttasil.sweetloc.data.userRoomsNode
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject


class RoomRepository @Inject constructor(
        private val userRepository: UserRepository,
        private val databaseReference: DatabaseReference
) {

    fun createRoom(roomName: String): Completable {
        return Completable.create {
            val scope = CoroutineScope(Dispatchers.IO)
            try {
                scope.launch {
                    val room = RoomEntity(roomName)
                    val user = userRepository.getUserEntity()
                    val roomId = addRoom(room).blockingGet()
                    addUserRoom(user!!.userId, roomId, room).blockingGet()
                    it.onComplete()
                    coroutineContext.cancel()
                }
            } catch (e: Exception) {
                scope.coroutineContext.cancel()
                it.onError(e)
            }
        }
    }

    fun addRoom(roomEntity: RoomEntity): Single<String> {
        return Single.create { emitter ->
            val record = databaseReference.child(roomsNode()).push()
            record.setValue(roomEntity)
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

    fun addUserRoom(userId: String, roomId: String, roomEntity: RoomEntity): Completable {
        return Completable.create { emitter ->
            val record = databaseReference.child(userRoomNode(userId, roomId))
            record.setValue(roomEntity)
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

    fun getAllRooms(): Single<List<RoomEntity>> {
        return Single.create<List<RoomEntity>> {
            databaseReference.child(roomsNode())
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            error.toException().printStackTrace()
                            it.onError(error.toException())
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val a = dataSnapshot.getValue(RoomEntity::class.java)
                        }
                    })
        }
    }

    fun getUserRooms(userId: String): Single<List<RoomEntity>> {
        return Single.create<List<RoomEntity>> { emitter ->
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (emitter.isDisposed) {
                        return
                    }
                    try {
                        val rooms = arrayListOf<RoomEntity>()
                        dataSnapshot.children.forEach {
                            val room = it.getValue(RoomEntity::class.java)
                            room?.roomId = it.key
                            rooms.add(room!!)
                        }

                        emitter.onSuccess(rooms.toList())
                    } catch (e: Exception) {
                        emitter.onError(DatabaseException("Problem in DB", e))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    if (emitter.isDisposed) {
                        return
                    }
                    emitter.onError(databaseError.toException())
                }
            }

            val childReference = databaseReference.child(userRoomsNode(userId))
            childReference.addListenerForSingleValueEvent(listener)
            emitter.setCancellable { childReference.removeEventListener(listener) }
        }.observeOn(Schedulers.io())
    }

    fun getRoomMembers(roomId: String): Single<List<UserEntity>> {
        return Single.create<List<UserEntity>> { emitter ->
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (emitter.isDisposed) {
                        return
                    }
                    try {
                        val users = arrayListOf<UserEntity>()
                        dataSnapshot.children.forEach {
                            val user = it.getValue(UserEntity::class.java)
                            users.add(user!!)
                        }

                        emitter.onSuccess(users.toList())
                    } catch (e: Exception) {
                        emitter.onError(DatabaseException("Problem in DB", e))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    if (emitter.isDisposed) {
                        return
                    }
                    emitter.onError(databaseError.toException())
                }
            }

            val childReference = databaseReference.child(roomMembersNode(roomId))
            childReference.addListenerForSingleValueEvent(listener)
            emitter.setCancellable { childReference.removeEventListener(listener) }
        }.observeOn(Schedulers.io())
    }

    fun isExistRoomName(name: String): Single<Boolean> {
        return Single.create<Boolean> { emitter ->
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (emitter.isDisposed) {
                        return
                    }
                    try {
                        dataSnapshot.children.forEach {
                            val room = it.getValue(RoomEntity::class.java)
                            if (room?.roomName == name) {
                                emitter.onSuccess(true)
                            }
                        }

                        emitter.onSuccess(false)
                    } catch (e: Exception) {
                        emitter.onError(DatabaseException("Problem in DB", e))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    if (emitter.isDisposed) {
                        return
                    }
                    emitter.onError(databaseError.toException())
                }
            }

            val childReference = databaseReference.child(roomsNode())
            childReference.addListenerForSingleValueEvent(listener)
            emitter.setCancellable { childReference.removeEventListener(listener) }
        }.observeOn(Schedulers.io())
    }

}