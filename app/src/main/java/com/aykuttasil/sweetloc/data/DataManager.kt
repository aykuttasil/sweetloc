package com.aykuttasil.sweetloc.data

import com.aykuttasil.sweetloc.data.local.entity.LocationEntity
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.data.local.entity.UserTrackerEntity
import com.aykuttasil.sweetloc.data.repository.LocationRepository
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.data.repository.UserTrackerRepository
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by aykutasil on 22.01.2018.
 */
class DataManager @Inject constructor(private val userRepository: UserRepository,
                                      private val locationRepository: LocationRepository,
                                      private val userTrackerRepository: UserTrackerRepository) {

    fun loginUser(username: String, password: String): Single<FirebaseUser> {
        return userRepository.loginUser(username, password)
    }

    fun registerUser(username: String, password: String): Single<FirebaseUser> {
        return userRepository.registerUser(username, password)
    }

    fun addUser(userEntity: UserEntity): Completable {
        return userRepository.addUser(userEntity)
    }

    fun updateUser(userEntity: UserEntity): Completable {
        return userRepository.updateUser(userEntity)
    }

    fun getUser(): Single<UserEntity?> {
        return userRepository.getUser()
    }

    fun getUserEntity(): UserEntity? {
        return userRepository.getUserEntity()
    }

    fun deleteUser(userEntity: UserEntity): Completable {
        return userRepository.deleteUser(userEntity)
    }

    fun addLocation(userId: String, locationEntity: LocationEntity): Completable {
        return locationRepository.addItem(userId, locationEntity)
    }

    fun addUserTrackerEntity(userTrackerEntity: UserTrackerEntity): Completable {
        return userTrackerRepository.addUserTracker(userTrackerEntity)
    }

    fun getUserTrackers(userId: String): Observable<List<UserTrackerEntity>> {
        return userTrackerRepository.getTrackerList(userId)
    }
}