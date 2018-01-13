package com.aykuttasil.sweetloc.di.modules

import android.arch.lifecycle.ViewModelProvider
import com.aykuttasil.sweetloc.di.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
internal abstract class ViewModelModule {

    /*
    @IntoMap
    @Binds
    @ViewModelKey(MainViewModel::class)
    abstract fun provideDetailVieWModel(viewModel: MainViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(UserViewModel::class)
    abstract fun provideUserVieWModel(viewModel: UserViewModel): ViewModel
    */

    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}

