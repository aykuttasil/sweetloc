package com.aykuttasil.sweetloc.data.repository

import aykuttasil.com.myviewmodelskeleton.data.local.dao.UserDao
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.data.remote.ApiManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by aykutasil on 22.01.2018.
 */
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val apiManager: ApiManager,
    private val firebaseAuth: FirebaseAuth
) {

    fun loginUser(
        username: String,
        password: String
    ): Single<FirebaseUser> {
        return Single.create<FirebaseUser> { emitter ->
            firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnSuccessListener { success ->
                    val firebaseUser = success.user
                    
                    val userEntity = UserEntity(
                        userUUID = firebaseUser.uid,
                        userEmail = firebaseUser.email!!,
                        userPassword = password
                    )
                    addUser(userEntity)
                        .subscribe({
                            emitter.onSuccess(firebaseUser)
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
    ): Single<FirebaseUser> {
        return Single.create<FirebaseUser> { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnSuccessListener { success ->
                    val firebaseUser = success.user
                    val userEntity = UserEntity(
                        userUUID = firebaseUser.uid,
                        userEmail = firebaseUser.email!!,
                        userPassword = password
                    )

                    addUser(userEntity)
                        .subscribe({
                            emitter.onSuccess(firebaseUser)
                        }, {
                            emitter.onError(it)
                        })
                }
                .addOnFailureListener { e ->
                    emitter.onError(e)
                }
        }

        /*
        return apiManager.register(username, password)
            .observeOn(Schedulers.io())
            .flatMap {
                val userEntity = UserEntity(
                    userUUID = it.uid,
                    userEmail = it.email!!,
                    userPassword = password
                )

                Single.create<FirebaseUser> { emitter ->
                    addUser(userEntity)
                        .subscribe({
                            emitter.onSuccess(it)
                        }, {
                            emitter.onError(it)
                        })
                }

            }
        */
    }

    fun addUser(userEntity: UserEntity): Completable {
        return apiManager.upsertUser(userEntity)
            .observeOn(Schedulers.io())
            .doOnComplete {
                userDao.insertItem(userEntity)
            }
    }

    fun updateUser(userEntity: UserEntity): Completable {
        return apiManager.upsertUser(userEntity)
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
}