package aykuttasil.com.myviewmodelskeleton.data.local.dao

import android.arch.persistence.room.*
import com.aykuttasil.sweetloc.data.local.entity.UserEntity

@Dao
abstract class UserDao {

    /*
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertItems(list: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertItem(item: UserEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updatetItem(item: UserEntity)

    @Delete
    abstract fun deletetItem(item: UserEntity)

    @Query("SELECT count(*) FROM user")
    abstract fun getItemsCount(): Flowable<Int>

    @Query("SELECT * FROM user")
    abstract fun getItems(): Flowable<List<UserEntity>>

    @Query("SELECT * FROM user")
    abstract fun getItemsLiveData(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user WHERE UserId=:id")
    abstract fun getItem(id: Long): UserEntity

    @Insert
    abstract fun insertAll(vararg users: UserEntity)
    */

    @Query("SELECT count(*) FROM User")
    abstract fun getItemsCount(): Int?


    //@Query("SELECT * FROM user WHERE UserId=:id")
    //abstract fun getItemFromId(uuid: Long): UserEntity

    @Query("SELECT * FROM user")
    abstract fun getItem(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertItem(item: UserEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateItem(user: UserEntity)

    @Delete
    abstract fun deleteItem(item: UserEntity)

}