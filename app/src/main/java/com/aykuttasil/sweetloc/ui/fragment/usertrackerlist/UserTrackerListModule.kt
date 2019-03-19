package com.aykuttasil.sweetloc.ui.fragment.usertrackerlist

import android.content.Context
import com.aykuttasil.sweetloc.data.DataManager
import com.aykuttasil.sweetloc.util.adapter.UserTrackerListAdapter
import dagger.Module
import dagger.Provides

/**
 * Created by aykutasil on 26.01.2018.
 */
@Module
class UserTrackerListModule {

    @Provides
    fun provideAdapter(
        context: Context,
        dataManager: DataManager
    ): UserTrackerListAdapter = UserTrackerListAdapter(context, dataManager)
}