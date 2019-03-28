package com.aykuttasil.sweetloc.data.repository

import com.aykuttasil.sweetloc.data.local.dao.UserTrackerDao
import com.aykuttasil.sweetloc.data.local.entity.UserTrackerEntity
import com.aykuttasil.sweetloc.data.userRoomsNode
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserTrackerRepository @Inject constructor(
        private val userTrackerDao: UserTrackerDao,
        private val databaseReference: DatabaseReference
) {

    fun addUserTracker(userTrackerEntity: UserTrackerEntity): Completable {
        return Completable.create { emitter ->
            try {
                userTrackerDao.insertItem(userTrackerEntity)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(Exception("Kayıt Eklenemedi."))
            }
        }
    }

    fun getTrackerList(userId: String): Observable<List<UserTrackerEntity>> {
        return Observable.create { emitter: ObservableEmitter<List<UserTrackerEntity>> ->
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (emitter.isDisposed) {
                        return
                    }
                    try {
                        val listUserTrackerEntity = arrayListOf<UserTrackerEntity>()
                        dataSnapshot.children.forEach {
                            listUserTrackerEntity.add(it.getValue(UserTrackerEntity::class.java)!!)
                        }

                        emitter.onNext(listUserTrackerEntity)
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
}