package com.aykuttasil.sweetloc.ui.fragment.usertrackerlist

import android.content.Context
import com.aykuttasil.sweetloc.data.DataManager
import com.aykuttasil.sweetloc.di.scopes.PerFragment
import com.aykuttasil.sweetloc.util.adapter.UserTrackerListAdapter
import dagger.Module
import dagger.Provides

@Module
class UserTrackerListModule {

    @Provides
    fun provideAdapter(
            context: Context,
            dataManager: DataManager
    ): UserTrackerListAdapter = UserTrackerListAdapter(context, dataManager)

    @Provides
    @PerFragment
    fun provideContext(userTrackerListFragment: UserTrackerListFragment): Context {
        return userTrackerListFragment.context!!
    }
}