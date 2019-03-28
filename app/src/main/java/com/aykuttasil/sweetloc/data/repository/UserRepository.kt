package com.aykuttasil.sweetloc.data.repository

import aykuttasil.com.myviewmodelskeleton.data.local.dao.UserDao
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.data.userNode
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.onesignal.OneSignal
import com.orhanobut.logger.Logger
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val userDao: UserDao,
        private val firebaseAuth: FirebaseAuth,
        private val databaseReference: DatabaseReference
) {

    fun loginUser(
            username: String,
            password: String
    ): Single<UserEntity> {
        return Single.create<UserEntity> { emitter ->
            firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnSuccessListener { success ->
                        val firebaseUser = success.user
                        val userEntity = UserEntity(
                                userUUID = firebaseUser.uid,
                                userEmail = firebaseUser.email,
                                userPassword = password
                        )
                        addUser(userEntity)
                                .subscribe({
                                    emitter.onSuccess(userEntity)
                                }, {
                                    emitter.onError(it)
                                })
                    }
                    .addOnFailureListener { e ->
                        emitter.onError(e)
                    }
        }
    }

    fun registerUser(
            username: String,
            password: String
    ): Single<UserEntity> {
        return Single.create<UserEntity> { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(username, password)
                    .addOnSuccessListener { success ->
                        val firebaseUser = success.user
                        val userEntity = UserEntity(
                                userUUID = firebaseUser.uid,
                                userEmail = firebaseUser.email,
                                userPassword = password
                        )

                        addUser(userEntity)
                                .subscribe({
                                    emitter.onSuccess(userEntity)
                                }, {
                                    emitter.onError(it)
                                })
                    }
                    .addOnFailureListener { e ->
                        emitter.onError(e)
                    }
        }
    }

    fun addUser(userEntity: UserEntity): Completable {
        return upsertUser(userEntity)
                .observeOn(Schedulers.io())
                .doOnComplete {
                    userDao.insertItem(userEntity)
                }
    }

    fun updateUser(userEntity: UserEntity): Completable {
        return upsertUser(userEntity)
                .observeOn(Schedulers.io())
                .doOnComplete {
                    userDao.updateItem(userEntity)
                }
    }

    fun deleteUser(userEntity: UserEntity): Completable {
        return Completable.create {
            try {
                userDao.deleteItem(userEntity)
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    private fun upsertUser(userId: String, action: (DatabaseReference) -> Task<Void>): Completable {
        return Completable.create { emitter ->
            action(databaseReference.child(userNode(userId)))
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

    fun upsertUser(userEntity: UserEntity): Completable {
        return upsertUser(userEntity.userUUID) {
            it.setValue(userEntity)
        }
    }

    fun getUser1(): UserEntity? = runBlocking {
        return@runBlocking withContext(Dispatchers.IO) { userDao.getItem() }
    }

    fun getUser(): Single<UserEntity?> {
        return Single.create<UserEntity> {
            if (userDao.getItem() != null) {
                it.onSuccess(userDao.getItem()!!)
            } else {
                it.onError(Exception("Kullanıcı yok"))
            }
        }
    }

    fun getUserEntity(): UserEntity? {
        return userDao.getItem()
    }

    fun checkUser(): Boolean {
        return runBlocking {
            val userEntity = withContext(Dispatchers.Default) { getUserEntity() }
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            return@runBlocking userEntity != null && firebaseUser != null
        }
    }

    fun updateOneSignalId() {
        OneSignal.idsAvailable { userId, registrationId ->
            Logger.i("OneSignal userId: $userId")
            Logger.i("OneSignal regId: $registrationId")

            val user = getUserEntity()?.apply {
                userOneSignalId = userId
            }

            user?.apply {
                updateUser(this).blockingAwait()
            }
        }
    }
}