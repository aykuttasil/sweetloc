package com.aykuttasil.sweetloc.ui.fragment.roomcreate

import androidx.lifecycle.MutableLiveData
import com.aykuttasil.sweetloc.App
import com.aykuttasil.sweetloc.data.Room
import com.aykuttasil.sweetloc.data.repository.RoomRepository
import com.aykuttasil.sweetloc.util.BaseAndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RoomCreateViewModel @Inject constructor(
        val app: App,
        val roomRepository: RoomRepository
) : BaseAndroidViewModel(app) {

    val liveProgress = MutableLiveData<Boolean>()
    val liveSnackbar = MutableLiveData<String>()


    fun createRoom(roomName: String) {

        /*
        roomRepository.createRoom(roomName).subscribe({
            liveSnackbar.value = "Room added."
        }, {
            it.printStackTrace()
        }).addTo(disposables)

         */



        launch {
            liveProgress.value = true
            println("Coroutine Context:$coroutineContext")
            delay(3000)
            val roomId = withContext(Dispatchers.IO) {
                println("Coroutine Context1:$coroutineContext")
                roomRepository.addRoom(Room(roomName)).blockingGet()
            }
            println("Coroutine Context2:$coroutineContext")
            liveSnackbar.value = "Room added."
            println("Room Id:$roomId")
            liveProgress.value = false
        }

        /*
        roomRepository.addRoom(Room(roomName)).subscribe({
            liveSnackbar.value = "Room added."
            println("Room Id:$it")
        }, { it.printStackTrace() }).addTo(disposables)

         */
    }

}
