package com.aykuttasil.sweetloc.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import aykuttasil.com.myviewmodelskeleton.data.local.dao.LocationDao
import aykuttasil.com.myviewmodelskeleton.data.local.dao.UserDao
import com.aykuttasil.sweetloc.data.local.dao.UserTrackerDao
import com.aykuttasil.sweetloc.data.local.entity.LocationEntity
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.data.local.entity.UserTrackerEntity
import com.aykuttasil.sweetloc.util.converter.RoomTypeConverter

/**
 * Created by aykutasil on 22.01.2018.
 */
@Database(
        entities = [(UserEntity::class),
            (LocationEntity::class),
            (UserTrackerEntity::class)],
        version = 1)
@TypeConverters(RoomTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getLocationDao(): LocationDao

    abstract fun getUserTrackerDao(): UserTrackerDao
}