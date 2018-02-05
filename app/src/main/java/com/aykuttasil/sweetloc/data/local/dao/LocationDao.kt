package aykuttasil.com.myviewmodelskeleton.data.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.aykuttasil.sweetloc.data.local.entity.LocationEntity
import io.reactivex.Flowable

/**
 * Created by aykutasil on 27.12.2017.
 */
@Dao
abstract class LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertItem(item: LocationEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updatetItem(item: LocationEntity)

    @Query("SELECT * FROM Location")
    abstract fun getItems(): Flowable<List<LocationEntity>>

    @Query("SELECT * FROM Location")
    abstract fun getItemsLiveData(): LiveData<List<LocationEntity>>

    @Query("SELECT * FROM Location WHERE locationId=:id")
    abstract fun getItem(id: Long): LocationEntity


    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertItems(list: List<LocationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertItem(item: LocationEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updatetItem(item: LocationEntity)

    @Delete
    abstract fun deleteItem(item: LocationEntity)

    @Query("SELECT * FROM sendMyLocation")
    abstract fun getItems(): Flowable<List<LocationEntity>>

    @Query("SELECT * FROM sendMyLocation")
    abstract fun getItemsLiveData(): LiveData<List<LocationEntity>>

    @Query("SELECT * FROM sendMyLocation WHERE locationId=:id")
    abstract fun getItem(id: Long): LocationEntity

    @Insert
    abstract fun insertAll(vararg locations: LocationEntity)
    */
}