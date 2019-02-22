package com.aykuttasil.sweetloc.di.components

import android.app.Application
import com.aykuttasil.sweetloc.app.App
import com.aykuttasil.sweetloc.di.ActivityBuilder
import com.aykuttasil.sweetloc.di.modules.AppModule
import com.aykuttasil.sweetloc.di.modules.DatabaseModule
import com.aykuttasil.sweetloc.di.modules.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by aykutasil on 8.12.2017.
 */
@Singleton
@Component(modules = [
    (AndroidSupportInjectionModule::class),
    (ActivityBuilder::class),
    (AppModule::class),
    (NetworkModule::class),
    (DatabaseModule::class)])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
