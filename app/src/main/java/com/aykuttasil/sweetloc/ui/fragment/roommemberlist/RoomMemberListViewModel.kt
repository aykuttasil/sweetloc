package com.aykuttasil.sweetloc.ui.fragment.roommemberlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aykuttasil.sweetloc.data.UserModel
import com.aykuttasil.sweetloc.data.repository.RoomRepository
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await
import javax.inject.Inject

class RoomMemberListViewModel @Inject constructor(
    private val app: Application,
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository
) : BaseAndroidViewModel(app) {

    val liveRoomMemberList: MutableLiveData<List<UserModel>> = MutableLiveData()

    fun getRoomMemberList(roomId: String): LiveData<List<UserModel>> {
        roomRepository.getRoomMembers(roomId)
            .subscribe({
                liveRoomMemberList.postValue(it)
            }, {
                it.printStackTrace()
            }).addTo(disposables)

        return liveRoomMemberList
    }

    fun setRoomMemberList(roomId: String) {
        roomRepository.getRoomMembers(roomId)
            .subscribe({
                liveRoomMemberList.postValue(it)
            }, {
                it.printStackTrace()
            }).addTo(disposables)
    }

    fun addMember(roomId: String) {
        launch {
            val user = userRepository.getUserEntity()
            roomRepository.addRoomMember(user?.userId!!, roomId, user).await()
            val room = roomRepository.getRoom(roomId).await()
            roomRepository.addUserRoom(user.userId, roomId, room).await()
        }
    }
}