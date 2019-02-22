package com.aykuttasil.sweetloc.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aykuttasil.sweetloc.di.key.ViewModelKey
import com.aykuttasil.sweetloc.ui.activity.login.LoginViewModel
import com.aykuttasil.sweetloc.ui.activity.main.MainActivityViewModel
import com.aykuttasil.sweetloc.ui.activity.map.MapsViewModel
import com.aykuttasil.sweetloc.ui.activity.profile.ProfileViewModel
import com.aykuttasil.sweetloc.ui.fragment.usergroup.UserGroupsModule
import com.aykuttasil.sweetloc.ui.fragment.usergroup.UserGroupsViewModel
import com.aykuttasil.sweetloc.ui.fragment.usertrackerlist.UserTrackerListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by aykutasil on 25.01.2018.
 */
@Module
abstract class ViewModelBuilder {

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
    @ViewModelKey(ProfileViewModel::class)
    abstract fun provideProfileVieWModel(viewModel: ProfileViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(MapsViewModel::class)
    abstract fun provideMapsViewModel(viewModel: MapsViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(UserTrackerListViewModel::class)
    abstract fun provideUserTrackerListViewModel(viewModel: UserTrackerListViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(UserGroupsViewModel::class)
    abstract fun provideUserGroupsViewModel(viewModel: UserGroupsViewModel): ViewModel


    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}