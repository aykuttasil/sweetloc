/* Author - Aykut Asil(aykuttasil) */
package com.aykuttasil.sweetloc.ui.fragment.roomlist

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.aykuttasil.sweetloc.data.RoomEntity
import com.aykuttasil.sweetloc.data.repository.RoomRepository
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomListViewModel @Inject constructor(
    private val app: Application,
    private val userRepository: UserRepository,
    private val roomRepository: RoomRepository
) : BaseAndroidViewModel(app) {

    val liveRoomEntityList: MutableLiveData<List<RoomEntity>> = MutableLiveData()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun getRoomList(): LiveData<List<RoomEntity>> {
        launch {
            val user = userRepository.getUserEntitySuspend()!!
            roomRepository.getUserRooms(user.userId)
                .subscribe({ roomList ->
                    liveRoomEntityList.postValue(roomList)
                }, {
                    it.printStackTrace()
                }).addTo(disposables)
        }
        return liveRoomEntityList
    }
}

// if (liveRoomEntityList.value != null) return liveRoomEntityList

/*
launch {
    val user = userRepository.getUserEntity()!!
    val roomList = withContext(Dispatchers.IO) { roomRepository.getUserRooms(user.userId).blockingGet() }
    liveRoomEntityList.postValue(roomList)
}

/*
launch {
    val user = userRepository.getUser().blockingGet()
    user?.let {
        // handler.postDelayed(runnable, 2000)
        userTrackerRepository.getTrackerList(it.userId).blockingForEach { list ->
            liveRoomEntityList.postValue(list)
        }
    }
}

 */

/*
//DbManager.deleteModelUserTrackerList()
val databaseReference = FirebaseDatabase.getInstance().reference
val queryUser = databaseReference.child(UserEntity::class.java.simpleName)

queryUser.addListenerForSingleValueEvent(object : ValueEventListener {
    @DebugLog
    override fun onDataChange(dataSnapshot: DataSnapshot) {
        bg {
            /*
            val me = DbManager.getModelUser()

            for (dataSnapshot1 in dataSnapshot.children) {
                val modelUser = dataSnapshot1.getValue(ModelUser::class.java)

                Logger.i("User: " + modelUser?.email + "\n UUID:" + modelUser?.uuid);
                Logger.i("Me: " + me?.email + "\n UUID: " + me?.uuid)

                // Aynı token a sahip diğer kullanıcılar buraya girer
                if (modelUser?.uuid != FirebaseAuth.getInstance().currentUser!!.uid && modelUser?.token == me.token) {
                    Logger.i("Add Item: " + modelUser?.email)
                    Logger.i("User Tracker OneSignalUserId: " + modelUser?.oneSignalUserId)

                    val modelUserTracker = ModelUserTracker()
                    modelUserTracker.email = modelUser?.email
                    modelUserTracker.ad = modelUser?.ad
                    modelUserTracker.soyAd = modelUser?.soyAd
                    modelUserTracker.profilePictureUrl = modelUser?.imageUrl
                    modelUserTracker.oneSignalUserId = modelUser?.oneSignalUserId
                    modelUserTracker.uuid = modelUser?.uuid
                    modelUserTracker.token = modelUser?.token
                    modelUserTracker.save()

                    mAdapter.addUserLocation(modelUserTracker)
                }
            }
            */
        }
    }

    override fun onCancelled(databaseError: DatabaseError) {}
})
*/

return liveRoomEntityList
}

override fun onCleared() {
super.onCleared()
job.cancel()
println("onCleared")
}

val handler = Handler()
private val runnable = object : Runnable {
override fun run() {
    liveRoomEntityList.value = mockUserTrackerList(2)
    handler.postDelayed(this, 2000)
}
}

fun mockUserTrackerList(size: Int): List<RoomEntity> {
val roomList = mutableListOf<RoomEntity>()
for (i in 1..size) {
    val room = RoomEntity().apply {
        this.roomName = "RoomEntity$i"
        this.roomOwner = "aykuttasil$i@hotmail.com"
    }
    roomList.add(room)
}
return roomList
}
}
 */