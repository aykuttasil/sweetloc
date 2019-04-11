package com.aykuttasil.sweetloc.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aykuttasil.sweetloc.di.key.ViewModelKey
import com.aykuttasil.sweetloc.ui.activity.login.LoginViewModel
import com.aykuttasil.sweetloc.ui.activity.main.MainActivityViewModel
import com.aykuttasil.sweetloc.ui.activity.map.MapsViewModel
import com.aykuttasil.sweetloc.ui.fragment.entry.EntryViewModel
import com.aykuttasil.sweetloc.ui.fragment.profile.ProfileViewModel
import com.aykuttasil.sweetloc.ui.fragment.roomcreate.RoomCreateViewModel
import com.aykuttasil.sweetloc.ui.fragment.roomlist.RoomListViewModel
import com.aykuttasil.sweetloc.ui.fragment.roommemberlist.RoomMemberListViewModel
import com.aykuttasil.sweetloc.ui.fragment.usergroup.UserGroupsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelBuilder {

    @IntoMap
    @Binds
    @ViewModelKey(EntryViewModel::class)
    abstract fun provideEntryViewModel(viewModel: EntryViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(LoginViewModel::class)
    abstract fun provideLoginViewModel(viewModel: LoginViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun provideMainActivityVieWModel(viewModel: MainActivityViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(MapsViewModel::class)
    abstract fun provideMapsViewModel(viewModel: MapsViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(RoomListViewModel::class)
    abstract fun provideRoomListViewModel(viewModel: RoomListViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(UserGroupsViewModel::class)
    abstract fun provideUserGroupsViewModel(viewModel: UserGroupsViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(RoomCreateViewModel::class)
    abstract fun provideRoomCreateViewModel(viewModel: RoomCreateViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(RoomMemberListViewModel::class)
    abstract fun provideRoomMemberListViewModel(viewModel: RoomMemberListViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ProfileViewModel::class)
    abstract fun provideProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}