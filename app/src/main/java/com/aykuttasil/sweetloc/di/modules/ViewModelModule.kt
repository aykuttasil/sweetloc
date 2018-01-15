package com.aykuttasil.sweetloc.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.aykuttasil.sweetloc.activity.main.MainActivityViewModel
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.di.key.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun provideMainActivityVieWModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}

