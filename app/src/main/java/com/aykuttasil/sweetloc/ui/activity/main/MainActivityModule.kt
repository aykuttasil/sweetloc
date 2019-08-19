/* Author - Aykut Asil(@aykuttasil) */
package com.aykuttasil.sweetloc.ui.activity.main

import android.content.Context
import com.aykuttasil.sweetloc.di.ActivityContext
import dagger.Module
import dagger.Provides

/**
 * Created by aykutasil on 15.01.2018.
 */
@Module
class MainActivityModule {

    @ActivityContext
    @Provides
    fun provideContext(activity: MainActivity): Context {
        return activity
    }
}
