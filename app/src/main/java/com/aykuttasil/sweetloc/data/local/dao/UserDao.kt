/* Author - Aykut Asil(aykuttasil) */
package com.aykuttasil.sweetloc.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aykuttasil.sweetloc.data.local.entity.UserEntity

@Dao
abstract class UserDao {

    @Query("SELECT count(*) FROM User")
    abstract fun getItemsCount(): Int?

    @Query("SELECT * FROM user")
    abstract fun getItem(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertItem(item: UserEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateItem(user: UserEntity)

    @Delete
    abstract fun deleteItem(item: UserEntity)
}
