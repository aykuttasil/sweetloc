package com.aykuttasil.sweetloc.data.remote

import com.aykuttasil.sweetloc.data.local.entity.LocationEntity
import com.aykuttasil.sweetloc.data.remote.model.UserData
import com.google.android.gms.tasks.Task
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

class FirebaseDbService @Inject constructor(private val database: DatabaseReference) {

    fun locations(): Observable<LocationEntity> {
        return observeChild(daysNode(), LocationEntity::class.java)
    }

    /*
        fun locations(userId: String): Observable<LocationEntity> {


            return userData(userId)
                    .map { (locations) -> locations.orEmpty() }

        }
        */

    private fun userData(userId: String): Observable<UserData> {
        return observeOptionalChild(userDataNode(userId), UserData::class.java, lazy { UserData() })
    }

    /*
    fun speakers(): Observable<FirebaseSpeakers> {
        return observeChild(speakersNode(), FirebaseSpeakers::class.java)
    }
    */

    private fun <T> observeChild(
        path: String,
        clazz: Class<T>
    ): Observable<T> {
        return observeChildAndEmit(path, clazz, { it!! })
    }

    private fun <T> observeOptionalChild(
        path: String,
        clazz: Class<T>,
        default: Lazy<T>
    ): Observable<T> {
        return observeChildAndEmit(path, clazz, { it ?: default.value })
    }

    private fun <T, V> observeChildAndEmit(
        path: String,
        clazz: Class<V>,
        map: (V?) -> T
    ): Observable<T> {
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
                        emitter.onError(
                            DatabaseException("Problem in DB at path $path, class $clazz", e))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    if (emitter.isDisposed) {
                        return
                    }
                    emitter.onError(databaseError.toException())
                }
            }

            val childReference = database.child(path)
            childReference.addValueEventListener(listener)
            emitter.setCancellable { childReference.removeEventListener(listener) }
        }.observeOn(Schedulers.io())
    }

    fun addFavorite(
        eventId: String,
        userId: String
    ): Completable {
        return updateFavorite(eventId, userId, { it.setValue(true) })
    }

    fun removeFavorite(
        eventId: String,
        userId: String
    ): Completable {
        return updateFavorite(eventId, userId) { it.removeValue() }
    }

    private fun updateFavorite(
        eventId: String,
        userId: String,
        action: (DatabaseReference) -> Task<Void>
    ): Completable {
        return Completable.create { emitter ->
            action(database.child(favoriteByIdNode(userId, eventId)))
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { e ->
                    if (emitter.isDisposed) {
                        return@addOnFailureListener
                    }
                    emitter.onError(e)
                }

        }
    }

    fun addAchievement(
        userId: String,
        achievementId: String,
        timestamp: Long?
    ): Completable {
        return Completable.create { emitter ->
            database.child(achievementByIdNode(userId, achievementId)).setValue(timestamp)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { e ->
                    if (emitter.isDisposed) {
                        return@addOnFailureListener
                    }

                    emitter.onError(e)
                }
        }
    }

    private fun daysNode() = "data/days"
    private fun speakersNode() = "data/speakers"
    private fun eventsNode() = "data/events"
    private fun eventByIdNode(eventId: String) = "data/events/events/$eventId"
    private fun placesNode() = "data/places"
    private fun tracksNode() = "data/tracks"
    private fun trackByIdNode(trackId: String) = "data/tracks/$trackId"
    private fun venueInfoNode() = "data/venue"
    private fun userDataNode(userId: String) = "user/$userId"
    private fun favoriteByIdNode(
        userId: String,
        eventId: String
    ) = "${userDataNode(userId)}/favorites/$eventId"

    private fun achievementByIdNode(
        userId: String,
        achievementId: String
    ) = "${userDataNode(userId)}/achievements/$achievementId"
}
