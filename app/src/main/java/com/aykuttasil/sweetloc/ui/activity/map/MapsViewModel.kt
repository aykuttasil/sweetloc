/* Author - Aykut Asil(@aykuttasil) */
package com.aykuttasil.sweetloc.ui.activity.map

import android.app.Application
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aykuttasil.sweetloc.data.RoomMemberLocationModel
import com.aykuttasil.sweetloc.data.repository.LocationRepository
import com.aykuttasil.sweetloc.data.repository.RoomRepository
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.data.toLocationEntity
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await
import javax.inject.Inject

class MapsViewModel @Inject constructor(
  private val app: Application,
  private val userRepository: UserRepository,
  private val locationRepository: LocationRepository,
  private val roomRepository: RoomRepository
) : BaseAndroidViewModel(app) {

    private val _liveRoomMemberLocation: MutableLiveData<RoomMemberLocationModel> = MutableLiveData()

    fun updateLocation(location: Location?) {
        launch {
            location?.let { loc ->
                userRepository.getUserEntitySuspend()?.let { user ->
                    val roomList = roomRepository.getUserRooms(user.userId).await()
                    locationRepository.addUserAndRoomLocation(user, loc.toLocationEntity(), roomList).await()
                }
            }
        }
    }

    fun getRoomMembersLocation(roomId: String): LiveData<RoomMemberLocationModel> {
        launch {
            locationRepository.getRoomMembersLocations(roomId).subscribe {
                _liveRoomMemberLocation.postValue(it)
            }.addTo(disposables)
        }
        return _liveRoomMemberLocation
    }
}
