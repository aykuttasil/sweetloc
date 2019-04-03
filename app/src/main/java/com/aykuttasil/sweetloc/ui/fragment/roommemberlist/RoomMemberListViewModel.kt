package com.aykuttasil.sweetloc.ui.fragment.roommemberlist

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.aykuttasil.sweetloc.App
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.data.repository.RoomRepository
import com.aykuttasil.sweetloc.util.BaseAndroidViewModel
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class RoomMemberListViewModel @Inject constructor(
        private val app: App,
        private val roomRepository: RoomRepository
) : BaseAndroidViewModel(app) {

    val liveRoomMemberList: MutableLiveData<List<UserEntity>> = MutableLiveData()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun getRoomMemberList(roomId: String): LiveData<List<UserEntity>> {
        roomRepository.getRoomMembers(roomId).subscribe({
            liveRoomMemberList.value = it
        }, {
            it.printStackTrace()
        }).addTo(disposables)

        return liveRoomMemberList
    }
}