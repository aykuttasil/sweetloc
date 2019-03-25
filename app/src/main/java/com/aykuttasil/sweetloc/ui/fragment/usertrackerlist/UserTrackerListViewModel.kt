package com.aykuttasil.sweetloc.ui.fragment.usertrackerlist

import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.aykuttasil.sweetloc.data.DataManager
import com.aykuttasil.sweetloc.data.local.entity.UserTrackerEntity
import com.aykuttasil.sweetloc.util.RxAwareViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserTrackerListViewModel @Inject constructor(private val dataManager: DataManager) : RxAwareViewModel(), LifecycleObserver {

    val liveUserTrackerEntity: MutableLiveData<List<UserTrackerEntity>> = MutableLiveData()


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getTrackerList(): LiveData<List<UserTrackerEntity>> {
        handler.postDelayed(runnable, 2000)

        /*
        disposables.add(dataManager.getUser()
                .flatMapObservable {
                    dataManager.getUserTrackers(it.userUUID)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    liveUserTrackerEntity.value = it
                }, {
                    liveUserTrackerEntity.value = emptyList()
                    it.printStackTrace()
                }))
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

                            mAdapter.addItem(modelUserTracker)
                        }
                    }
                    */
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        */

        return liveUserTrackerEntity
    }


    val handler = Handler()
    private val runnable = object : Runnable {
        override fun run() {
            liveUserTrackerEntity.value = mockUserTrackerList(2)
            handler.postDelayed(this, 2000)
        }
    }

    fun mockUserTrackerList(size: Int): List<UserTrackerEntity> {
        val userTrackerEntityList = mutableListOf<UserTrackerEntity>()
        for (i in 1..size) {
            val userTrackerEntity = UserTrackerEntity().apply {
                userTrackerId = i.toLong()
                email = "aykuttasil$i@hotmail.com"
                this.name = "Aykut$i"
            }
            userTrackerEntityList.add(userTrackerEntity)
        }
        return userTrackerEntityList
    }
}