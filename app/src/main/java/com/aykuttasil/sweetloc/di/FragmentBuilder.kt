package com.aykuttasil.sweetloc.di

import com.aykuttasil.sweetloc.di.scopes.PerFragment
import com.aykuttasil.sweetloc.ui.activity.profile.ProfileFragment
import com.aykuttasil.sweetloc.ui.fragment.main.EntryFragment
import com.aykuttasil.sweetloc.ui.fragment.roomcreate.RoomCreateFragment
import com.aykuttasil.sweetloc.ui.fragment.usergroup.UserGroupsFragment
import com.aykuttasil.sweetloc.ui.fragment.usergroup.UserGroupsModule
import com.aykuttasil.sweetloc.ui.fragment.usertrackerlist.UserTrackerListFragment
import com.aykuttasil.sweetloc.ui.fragment.usertrackerlist.UserTrackerListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @PerFragment
    @ContributesAndroidInjector
    abstract fun bindEntryFragment(): EntryFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun bindProfileActivityFragment(): ProfileFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [UserTrackerListModule::class])
    abstract fun bindUserTrackerListFragment(): UserTrackerListFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [UserGroupsModule::class])
    abstract fun bindUserGroupsFragment(): UserGroupsFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun bindRoomCreateFragment(): RoomCreateFragment
}