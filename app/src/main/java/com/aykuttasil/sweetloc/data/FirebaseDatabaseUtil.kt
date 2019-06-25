/* Author - Aykut Asil(aykuttasil) */
package com.aykuttasil.sweetloc.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers

class FirebaseDatabaseUtil constructor(val databaseReference: DatabaseReference) {

    fun <T> observeChild(path: String, clazz: Class<T>): Observable<T> {
        return observeChildAndEmit(path, clazz) { it!! }
    }

    fun <T> observeOptionalChild(path: String, clazz: Class<T>, default: Lazy<T>): Observable<T> {
        return observeChildAndEmit(path, clazz) { it ?: default.value }
    }

    fun <T, V> observeChildAndEmit(path: String, clazz: Class<V>, map: (V?) -> T): Observable<T> {
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

    fun <T, V> observeSingleChild(path: String, clazz: Class<V>, map: (V?) -> T): Observable<T> {
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
}