package com.aykuttasil.sweetloc.data.repository

import aykuttasil.com.myviewmodelskeleton.data.local.dao.LocationDao
import com.aykuttasil.sweetloc.data.local.entity.LocationEntity
import com.aykuttasil.sweetloc.data.remote.ApiManager
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by aykutasil on 24.01.2018.
 */
class LocationRepository @Inject constructor(private val locationDao: LocationDao, private val apiManager: ApiManager) {

    fun addItem(userId: String, locationEntity: LocationEntity): Completable {
        return apiManager.addLocation(userId, locationEntity)
                .observeOn(Schedulers.io())
                .doOnComplete({
                    locationDao.insertItem(locationEntity)
                })
    }
}