package com.aykuttasil.sweetloc.ui.fragment.roomcreate

import androidx.lifecycle.MutableLiveData
import com.aykuttasil.sweetloc.data.repository.RoomRepository
import com.aykuttasil.sweetloc.util.RxAwareViewModel
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject


class RoomCreateViewModel @Inject constructor(
        val roomRepository: RoomRepository
) : RxAwareViewModel() {

    val liveSnackbar = MutableLiveData<String>()

    fun createRoom(roomName: String) {
        roomRepository.createRoom(roomName).subscribe({
            liveSnackbar.value = "Room added."
        }, {
            it.printStackTrace()
        }).addTo(disposables)
    }

}
