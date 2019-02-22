package com.aykuttasil.sweetloc.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aykuttasil.sweetloc.data.local.entity.UserTrackerEntity
import io.reactivex.Flowable

/**
 * Created by aykutasil on 22.01.2018.
 */
@Dao
abstract class UserTrackerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertItem(item: UserTrackerEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updatetItem(item: UserTrackerEntity)

    @Query("SELECT * FROM UserTracker")
    abstract fun getItems(): Flowable<List<UserTrackerEntity>>

    @Query("SELECT * FROM UserTracker")
    abstract fun getItemsLiveData(): LiveData<List<UserTrackerEntity>>

    @Query("SELECT * FROM UserTracker WHERE userTrackerId=:id")
    abstract fun getItem(id: Long): UserTrackerEntity
}