package com.aykuttasil.sweetloc.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.db.DbManager
import com.aykuttasil.sweetloc.model.ModelUser
import com.aykuttasil.sweetloc.model.ModelUserTracker
import com.aykuttasil.sweetloc.util.adapter.UserTrackerListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.orhanobut.logger.Logger
import hugo.weaving.DebugLog
import kotlinx.android.synthetic.main.fragment_user_tracker_list_layout.*
import java.util.*

open class UserTrackerListFragment : BaseFragment() {

    lateinit var mList: List<ModelUserTracker>
    lateinit var mAdapter: UserTrackerListAdapter

    @DebugLog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mList = ArrayList()
        mAdapter = UserTrackerListAdapter(context, mList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vi = inflater.inflate(R.layout.fragment_user_tracker_list_layout, container, false)
        return vi
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerViewAdapter()
        setItems()
    }

    private fun initRecyclerViewAdapter() {
        RecyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        RecyclerView!!.adapter = mAdapter
    }


    @DebugLog
    private fun setItems() {
        DbManager.deleteModelUserTrackerList()
        val databaseReference = FirebaseDatabase.getInstance().reference
        val queryUser = databaseReference.child(ModelUser::class.java.simpleName)

        queryUser.addListenerForSingleValueEvent(object : ValueEventListener {
            @DebugLog
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val me = DbManager.getModelUser()

                for (dataSnapshot1 in dataSnapshot.children) {
                    val modelUser = dataSnapshot1.getValue(ModelUser::class.java)

                    Logger.i("User: " + modelUser!!.email)
                    Logger.i("User UUID: " + modelUser.uuid)
                    Logger.i("Me: " + me.email)
                    Logger.i("Me UUID: " + me.uuid)

                    // Aynı token a sahip diğer kullanıcılar buraya girer
                    if (modelUser.uuid != FirebaseAuth.getInstance().currentUser!!.uid && modelUser.token == me.token) {
                        Logger.i("Add Item: " + modelUser.email)
                        Logger.i("User Tracker OneSignalUserId: " + modelUser.oneSignalUserId)

                        val modelUserTracker = ModelUserTracker()
                        modelUserTracker.email = modelUser.email
                        modelUserTracker.ad = modelUser.ad
                        modelUserTracker.soyAd = modelUser.soyAd
                        modelUserTracker.profilePictureUrl = modelUser.imageUrl
                        modelUserTracker.oneSignalUserId = modelUser.oneSignalUserId
                        modelUserTracker.uuid = modelUser.uuid
                        modelUserTracker.token = modelUser.token
                        modelUserTracker.save()

                        mAdapter.addItem(modelUserTracker)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}
