package com.aykuttasil.sweetloc.ui.fragment.roomlist

import android.content.Context
import com.aykuttasil.sweetloc.data.repository.RoomRepository
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.di.scopes.PerFragment
import dagger.Module
import dagger.Provides

@Module
class RoomListModule {

    @Provides
    fun provideAdapter(context: Context,
                       userRepository: UserRepository,
                       roomRepository: RoomRepository
    ): RoomListAdapter = RoomListAdapter(context, userRepository, roomRepository)

    @Provides
    fun provideRoomsAdapter(context: Context,
                            userRepository: UserRepository,
                            roomRepository: RoomRepository
    ): RoomsAdapter = RoomsAdapter()

    @Provides
    @PerFragment
    fun provideContext(roomListFragment: RoomListFragment): Context {
        return roomListFragment.context!!
    }
}