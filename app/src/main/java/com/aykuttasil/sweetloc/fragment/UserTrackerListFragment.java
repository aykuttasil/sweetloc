package com.aykuttasil.sweetloc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.db.DbManager;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.aykuttasil.sweetloc.model.ModelUserTracker;
import com.aykuttasil.sweetloc.util.adapter.UserTrackerListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 11.12.2016.
 */

@EFragment(R.layout.fragment_user_tracker_list_layout)
public class UserTrackerListFragment extends BaseFragment {

    @ViewById(R.id.RecyclerView)
    RecyclerView mRecyclerView;

    List<ModelUserTracker> mList;
    UserTrackerListAdapter mAdapter;

    @DebugLog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<>();
        mAdapter = new UserTrackerListAdapter(getContext(), mList);
    }


    @DebugLog
    @AfterViews
    @Override
    void initializeAfterViews() {
        initRecyclerViewAdapter();
        setItems();
    }

    @DebugLog
    private void initRecyclerViewAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }


    @DebugLog
    private void setItems() {
        DbManager.deleteModelUserTrackerList();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query queryUser = databaseReference.child(ModelUser.class.getSimpleName());

        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @DebugLog
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ModelUser me = DbManager.getModelUser();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModelUser modelUser = dataSnapshot1.getValue(ModelUser.class);
                    Logger.i("User: " + modelUser.getEmail());
                    Logger.i("User UUID: " + modelUser.getUUID());
                    Logger.i("Me: " + me.getEmail());
                    Logger.i("Me UUID: " + me.getUUID());

                    // Aynı token a sahip diğer kullanıcılar buraya girer
                    if (!modelUser.getUUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) &&
                            modelUser.getToken().equals(me.getToken())) {
                        Logger.i("Add Item: " + modelUser.getEmail());
                        Logger.i("User Tracker OneSignalUserId: " + modelUser.getOneSignalUserId());

                        ModelUserTracker modelUserTracker = new ModelUserTracker();
                        modelUserTracker.setEmail(modelUser.getEmail());
                        modelUserTracker.setAd(modelUser.getAd());
                        modelUserTracker.setSoyAd(modelUser.getSoyAd());
                        modelUserTracker.setProfilePictureUrl(modelUser.getImageUrl());
                        modelUserTracker.setOneSignalUserId(modelUser.getOneSignalUserId());
                        modelUserTracker.setUUID(modelUser.getUUID());
                        modelUserTracker.setToken(modelUser.getToken());
                        modelUserTracker.save();
                        mAdapter.addItem(modelUserTracker);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
