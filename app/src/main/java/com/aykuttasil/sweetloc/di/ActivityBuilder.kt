package com.aykuttasil.sweetloc.di

import com.aykuttasil.sweetloc.activity.login.LoginActivityModule
import com.aykuttasil.sweetloc.activity.login.LoginActivity_
import com.aykuttasil.sweetloc.activity.main.MainActivityModule
import com.aykuttasil.sweetloc.activity.main.MainActivity_
import com.aykuttasil.sweetloc.activity.map.MapsActivityModule
import com.aykuttasil.sweetloc.activity.map.MapsActivity_
import com.aykuttasil.sweetloc.activity.profile.ProfileActivityModule
import com.aykuttasil.sweetloc.activity.profile.ProfileActivity_
import com.aykuttasil.sweetloc.di.scopes.PerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by aykutasil on 13.12.2017.
 */
@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = [(LoginActivityModule::class)])
    abstract fun bindLoginActivity(): LoginActivity_

    @PerActivity
    @ContributesAndroidInjector(modules = [(ProfileActivityModule::class)])
    abstract fun bindProfileActivity(): ProfileActivity_

    @PerActivity
    @ContributesAndroidInjector(modules = [(MapsActivityModule::class)])
    abstract fun bindMapsActivity(): MapsActivity_

    @PerActivity
    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    abstract fun bindMainActivity(): MainActivity_
}