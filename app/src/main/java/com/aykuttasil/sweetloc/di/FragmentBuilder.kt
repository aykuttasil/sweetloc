package com.aykuttasil.sweetloc.di

import com.aykuttasil.sweetloc.di.scopes.PerFragment
import com.aykuttasil.sweetloc.ui.activity.profile.ProfileFragment
import com.aykuttasil.sweetloc.ui.fragment.usertrackerlist.UserTrackerListFragment
import com.aykuttasil.sweetloc.ui.fragment.usertrackerlist.UserTrackerListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by aykutasil on 24.01.2018.
 */
@Module
abstract class FragmentBuilder {

    @PerFragment
    @ContributesAndroidInjector
    abstract fun bindProfileActivityFragment(): ProfileFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [UserTrackerListModule::class])
    abstract fun bindUserTrackerListFragment(): UserTrackerListFragment
}