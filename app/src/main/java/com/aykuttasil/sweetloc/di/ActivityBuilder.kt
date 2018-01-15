package com.aykuttasil.sweetloc.di

import com.aykuttasil.sweetloc.activity.login.LoginActivity
import com.aykuttasil.sweetloc.activity.login.LoginActivityModule
import com.aykuttasil.sweetloc.activity.main.MainActivity
import com.aykuttasil.sweetloc.activity.main.MainActivityModule
import com.aykuttasil.sweetloc.activity.map.MapsActivity
import com.aykuttasil.sweetloc.activity.map.MapsActivityModule
import com.aykuttasil.sweetloc.activity.profile.ProfileActivity
import com.aykuttasil.sweetloc.activity.profile.ProfileActivityModule
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
    abstract fun bindLoginActivity(): LoginActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(ProfileActivityModule::class)])
    abstract fun bindProfileActivity(): ProfileActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(MapsActivityModule::class)])
    abstract fun bindMapsActivity(): MapsActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(MainActivityModule::class)])
    abstract fun bindMainActivity(): MainActivity
}