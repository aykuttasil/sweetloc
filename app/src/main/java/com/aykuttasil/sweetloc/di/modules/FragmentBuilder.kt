/* Author - Aykut Asil(aykuttasil) */
package com.aykuttasil.sweetloc.di.modules

import com.aykuttasil.sweetloc.di.scopes.PerFragment

import com.aykuttasil.sweetloc.ui.fragment.entry.EntryFragment
import com.aykuttasil.sweetloc.ui.fragment.profile.ProfileFragment
import com.aykuttasil.sweetloc.ui.fragment.roomcreate.RoomCreateFragment
import com.aykuttasil.sweetloc.ui.fragment.roomlist.RoomListFragment
import com.aykuttasil.sweetloc.ui.fragment.roomlist.RoomListModule
import com.aykuttasil.sweetloc.ui.fragment.roommemberlist.RoomMemberListFragment
import com.aykuttasil.sweetloc.ui.fragment.usergroup.UserGroupsFragment
import com.aykuttasil.sweetloc.ui.fragment.usergroup.UserGroupsModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @PerFragment
    @ContributesAndroidInjector
    abstract fun bindEntryFragment(): EntryFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [RoomListModule::class])
    abstract fun bindRoomListFragment(): RoomListFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [UserGroupsModule::class])
    abstract fun bindUserGroupsFragment(): UserGroupsFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun bindRoomCreateFragment(): RoomCreateFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun bindRoomMemberListFragment(): RoomMemberListFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun bindProfileFragment1(): ProfileFragment
}