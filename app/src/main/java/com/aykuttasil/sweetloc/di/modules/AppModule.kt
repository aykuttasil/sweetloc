package com.aykuttasil.sweetloc.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.aykuttasil.sweetloc.App
import com.aykuttasil.sweetloc.di.ApplicationContext
import com.aykuttasil.sweetloc.di.ViewModelBuilder
import com.patloew.rxlocation.RxLocation
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by aykutasil on 8.12.2017.
 */

@Module(includes = [(ViewModelBuilder::class)])
class AppModule {

    @Singleton
    @Provides
    @ApplicationContext
    internal fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    internal fun provideApp(application: Application): App {
        return application.applicationContext as App
    }

    @Singleton
    @Provides
    internal fun provideSharedPreference(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
    }

    @Singleton
    @Provides
    internal fun provideRxLocation(application: Application): RxLocation {
        return RxLocation(application)
    }
}
