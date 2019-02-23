package com.aykuttasil.sweetloc.ui.fragment.usertrackerlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aykuttasil.sweetloc.data.DataManager
import com.aykuttasil.sweetloc.data.local.entity.UserTrackerEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by aykutasil on 25.01.2018.
 */
class UserTrackerListViewModel @Inject constructor(private val dataManager: DataManager) :
    ViewModel() {

    private val liveUserTrackerEntity: MutableLiveData<List<UserTrackerEntity>> = MutableLiveData()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getTrackerList(): LiveData<List<UserTrackerEntity>> {
        compositeDisposable.add(dataManager.getUser()
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

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}