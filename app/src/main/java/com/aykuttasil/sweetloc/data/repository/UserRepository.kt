package com.aykuttasil.sweetloc.data.repository

import com.aykuttasil.sweetloc.data.local.dao.UserDao
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.data.userNode
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.onesignal.OneSignal
import com.orhanobut.logger.Logger
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val firebaseAuth: FirebaseAuth,
        private val databaseReference: DatabaseReference,
        private val userDao: UserDao
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
                                userId = firebaseUser.uid,
                                userEmail = firebaseUser.email,
                                userPassword = password
                        )
                        emitter.onSuccess(userEntity)
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
                                userId = firebaseUser.uid,
                                userEmail = firebaseUser.email,
                                userPassword = password
                        )
                        emitter.onSuccess(userEntity)
                    }
                    .addOnFailureListener { e ->
                        emitter.onError(e)
                    }
        }
    }

    fun addUserToLocal(user: UserEntity) {
        userDao.insertItem(user)
    }

    fun deleteUserFromLocal(user: UserEntity): Completable {
        return Completable.create {
            try {
                userDao.deleteItem(user)
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    fun updateUserFromLocal(user: UserEntity): Completable {
        return Completable.create {
            try {
                userDao.updateItem(user)
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    fun processUserToRemote(userId: String, action: (DatabaseReference) -> Task<Void>): Completable {
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

    fun upsertUserToRemote(userEntity: UserEntity): Single<UserEntity> {
        return processUserToRemote(userEntity.userId) {
            it.setValue(userEntity)
        }.toSingle {
            userEntity
        }

    }

    fun getUserEntity(): UserEntity? = runBlocking {
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

    fun checkUser(): Boolean {
        val userEntity = getUserEntity()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        return userEntity != null && firebaseUser != null
    }

    fun updateOneSignalId() = runBlocking {
        withContext(Dispatchers.Default) {
            OneSignal.idsAvailable { userId, registrationId ->
                Logger.i("OneSignal userId: $userId")
                Logger.i("OneSignal regId: $registrationId")

                val user = getUserEntity()?.apply {
                    userOneSignalId = userId
                }

                user?.apply {
                    upsertUserToRemote(this).blockingGet()
                    updateUserFromLocal(this).blockingGet()
                }
            }
        }
    }
}