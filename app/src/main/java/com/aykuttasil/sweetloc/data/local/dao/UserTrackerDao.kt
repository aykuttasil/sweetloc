package com.aykuttasil.sweetloc.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aykuttasil.sweetloc.data.local.entity.UserTrackerEntity
import io.reactivex.Flowable

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