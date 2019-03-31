package com.aykuttasil.sweetloc.ui.fragment.roomlist

import android.content.Context
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.data.repository.UserTrackerRepository
import com.aykuttasil.sweetloc.di.scopes.PerFragment
import com.aykuttasil.sweetloc.util.adapter.UserTrackerListAdapter
import dagger.Module
import dagger.Provides

@Module
class RoomListModule {

    @Provides
    fun provideAdapter(context: Context,
                       userRepository: UserRepository,
                       userTrackerRepository: UserTrackerRepository
    ): UserTrackerListAdapter = UserTrackerListAdapter(context, userRepository, userTrackerRepository)

    @Provides
    @PerFragment
    fun provideContext(roomListFragment: RoomListFragment): Context {
        return roomListFragment.context!!
    }
}