package com.aykuttasil.sweetloc.ui.fragment.roommemberlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aykuttasil.sweetloc.App
import com.aykuttasil.sweetloc.data.UserModel
import com.aykuttasil.sweetloc.data.repository.RoomRepository
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class RoomMemberListViewModel @Inject constructor(
        private val app: App,
        private val roomRepository: RoomRepository
) : BaseAndroidViewModel(app) {

    val liveRoomMemberList: MutableLiveData<List<UserModel>> = MutableLiveData()

    fun getRoomMemberList(roomId: String): LiveData<List<UserModel>> {
        roomRepository.getRoomMembers(roomId).subscribe({
            liveRoomMemberList.postValue(it)
        }, {
            it.printStackTrace()
        }).addTo(disposables)

        return liveRoomMemberList
    }

    fun setRoomMemberList(roomId: String) {
        roomRepository.getRoomMembers(roomId).subscribe({
            liveRoomMemberList.postValue(it)
        }, {
            it.printStackTrace()
        }).addTo(disposables)
    }
}