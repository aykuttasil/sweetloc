package com.aykuttasil.sweetloc.data.repository

import com.aykuttasil.sweetloc.data.local.dao.UserTrackerDao
import com.aykuttasil.sweetloc.data.local.entity.UserTrackerEntity
import com.aykuttasil.sweetloc.data.remote.ApiManager
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by aykutasil on 25.01.2018.
 */
class UserTrackerRepository @Inject constructor(private val userTrackerDao: UserTrackerDao, private val apiManager: ApiManager) {

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
        return apiManager.getUserTrackerList(userId)
    }

}