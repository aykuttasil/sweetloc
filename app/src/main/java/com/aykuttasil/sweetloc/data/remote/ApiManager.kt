package com.aykuttasil.sweetloc.data.remote

import com.aykuttasil.sweetloc.data.local.entity.LocationEntity
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.data.local.entity.UserTrackerEntity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ApiManager @Inject constructor(private val databaseReference: DatabaseReference, private val firebaseAuth: FirebaseAuth) {

    fun login(username: String, password: String): Single<FirebaseUser> {
        return Single.create {
            firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnSuccessListener { success ->
                        it.onSuccess(success.user)
                    }
                    .addOnFailureListener { e ->
                        it.onError(e)
                    }
        }
    }

    fun register(username: String, password: String): Single<FirebaseUser> {
        return Single.create {
            firebaseAuth.createUserWithEmailAndPassword(username, password)
                    .addOnSuccessListener { success ->
                        it.onSuccess(success.user)
                    }
                    .addOnFailureListener { e ->
                        it.onError(e)
                    }
        }
    }

    fun upsertUser(userEntity: UserEntity): Completable {
        return upsertUser(userEntity.userUUID) { it.setValue(userEntity) }
    }

    fun addLocation(userId: String, locationEntity: LocationEntity): Completable {
        return Completable.create { emitter ->
            databaseReference.child(userLocationNode(userId))
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

    fun getUserTrackerList(userId: String): Observable<List<UserTrackerEntity>> {
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

            val childReference = databaseReference.child(userTrackerNode(userId))
            childReference.addListenerForSingleValueEvent(listener)
            emitter.setCancellable { childReference.removeEventListener(listener) }
        }
                .observeOn(Schedulers.io())

        //return observeChild(userTrackerNode(userId), UserTrackerEntity::class.java)
    }

    private fun <T> observeChild(path: String, clazz: Class<T>): Observable<T> {
        return observeChildAndEmit(path, clazz, { it!! })
    }

    private fun <T> observeOptionalChild(path: String, clazz: Class<T>, default: Lazy<T>): Observable<T> {
        return observeChildAndEmit(path, clazz, { it ?: default.value })
    }

    private fun <T, V> observeChildAndEmit(path: String, clazz: Class<V>, map: (V?) -> T): Observable<T> {
        return Observable.create { emitter: ObservableEmitter<T> ->
            val listener = object : ValueEventListener {
                @Suppress("TooGenericExceptionCaught") // We want to add info to *any* problems
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (emitter.isDisposed) {
                        return
                    }
                    try {
                        val value = dataSnapshot.getValue(clazz)
                        emitter.onNext(map(value))
                    } catch (e: Exception) {
                        emitter.onError(DatabaseException("Problem in DB at path $path, class $clazz", e))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    if (emitter.isDisposed) {
                        return
                    }
                    emitter.onError(databaseError.toException())
                }
            }

            val childReference = databaseReference.child(path)
            childReference.addValueEventListener(listener)
            emitter.setCancellable { childReference.removeEventListener(listener) }
        }.observeOn(Schedulers.io())
    }

    private fun <T, V> observeSingleChild(path: String, clazz: Class<V>, map: (V?) -> T): Observable<T> {
        return Observable.create { emitter: ObservableEmitter<T> ->
            val listener = object : ValueEventListener {
                @Suppress("TooGenericExceptionCaught") // We want to add info to *any* problems
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (emitter.isDisposed) {
                        return
                    }
                    try {
                        val value = dataSnapshot.getValue(clazz)
                        emitter.onNext(map(value))
                    } catch (e: Exception) {
                        emitter.onError(DatabaseException("Problem in DB at path $path, class $clazz", e))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    if (emitter.isDisposed) {
                        return
                    }
                    emitter.onError(databaseError.toException())
                }
            }

            val childReference = databaseReference.child(path)
            childReference.addListenerForSingleValueEvent(listener)
            emitter.setCancellable { childReference.removeEventListener(listener) }
        }.observeOn(Schedulers.io())
    }

    private fun upsertUser(userId: String, action: (DatabaseReference) -> Task<Void>): Completable {
        return Completable.create { emitter ->
            action(databaseReference.child(userDataNode(userId)))
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


    private fun userDataNode(userId: String) = "user/$userId"
    private fun userLocationNode(userId: String) = "${userDataNode(userId)}/locations"
    private fun userTrackerNode(userId: String) = "${userDataNode(userId)}/trackers"


}

