package com.aykuttasil.sweetloc.di.modules

import dagger.Module

/**
 * Created by aykutasil on 20.12.2017.
 */
@Module
class DatabaseModule {

    companion object {
        val DB_NAME = "aa.db"
    }

    /*@Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase) = db.getUserDao()
    */

}