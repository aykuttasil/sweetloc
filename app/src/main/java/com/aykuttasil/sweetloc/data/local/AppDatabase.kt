package com.aykuttasil.sweetloc.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aykuttasil.sweetloc.data.local.dao.UserDao
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.util.converter.RoomTypeConverter


@Database(
        entities = [
            (UserEntity::class)
            //(LocationEntity::class),
            //(Room::class)
        ],
        version = 1)
@TypeConverters(RoomTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao
    //abstract fun getLocationDao(): LocationDao
    //abstract fun getUserTrackerDao(): UserTrackerDao
}
