package com.aykuttasil.sweetloc.ui.fragment.roomcreate

import androidx.lifecycle.MutableLiveData
import com.aykuttasil.sweetloc.App
import com.aykuttasil.sweetloc.data.Room
import com.aykuttasil.sweetloc.data.repository.RoomRepository
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.util.BaseAndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomCreateViewModel @Inject constructor(
        val app: App,
        val userRepository: UserRepository,
        val roomRepository: RoomRepository
) : BaseAndroidViewModel(app) {

    val liveProgress = MutableLiveData<Boolean>()
    val liveSnackbar = MutableLiveData<String>()

    fun createRoom(roomName: String) {
        launch {
            val isExistRoomName = withContext(Dispatchers.Default) { roomRepository.isExistRoomName(roomName).blockingGet() }
            if (!isExistRoomName) {
                val room = Room(roomName)
                val user = userRepository.getUser1()
                val roomId = withContext(Dispatchers.Default) { roomRepository.addRoom(room).blockingGet() }
                val x = withContext(Dispatchers.Default) { roomRepository.addUserRoom(user!!.userUUID, roomId, room).blockingGet() }
                if (x == null) {
                    liveSnackbar.value = "Room added."
                } else {
                    liveSnackbar.value = "Room isn't added."
                }
            } else {
                liveSnackbar.value = "Room isn't added."
            }
        }
    }

}
