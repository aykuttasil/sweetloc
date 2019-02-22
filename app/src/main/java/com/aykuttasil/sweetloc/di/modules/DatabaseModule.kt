package com.aykuttasil.sweetloc.di.modules

import androidx.room.Room
import android.content.Context
import com.aykuttasil.sweetloc.data.local.AppDatabase
import com.aykuttasil.sweetloc.di.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [FirebaseModule::class])
class DatabaseModule {

    companion object {
        const val DB_NAME = "sweetLoc_12.db"
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java,
            DB_NAME).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase) = db.getUserDao()

    @Provides
    @Singleton
    fun provideLocationDao(db: AppDatabase) = db.getLocationDao()

    @Provides
    @Singleton
    fun provideUserTrackerDao(db: AppDatabase) = db.getUserTrackerDao()
}