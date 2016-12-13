package com.aykuttasil.sweetloc.db;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.aykuttasil.sweetloc.model.ModelSweetLocPreference;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.aykuttasil.sweetloc.model.ModelUserTracker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 10.07.2016.
 */
public class DbManager {

    @DebugLog
    public static ModelSweetLocPreference getModelSweetLocPreference() {
        return new Select().from(ModelSweetLocPreference.class).executeSingle();
    }

    @DebugLog
    public static ModelUser getModelUser() {
        return new Select().from(ModelUser.class).executeSingle();
    }


    @DebugLog
    public static Future<List<ModelUserTracker>> getUserList() {

        return new Future<List<ModelUserTracker>>() {
            @Override
            public boolean cancel(boolean b) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public List<ModelUserTracker> get() throws InterruptedException, ExecutionException {

                List<ModelUserTracker> modelUserTrackers = new ArrayList<>();

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

                            ModelUserTracker modelUserTracker = new ModelUserTracker();
                            modelUserTracker.setEmail(modelUser.getEmail());
                            modelUserTracker.setAd(modelUser.getAd());
                            modelUserTracker.setSoyAd(modelUser.getSoyAd());
                            modelUserTracker.setProfilePictureUrl("");

                            modelUserTrackers.add(modelUserTracker);
                    /*
                    // Aynı token a sahip diğer kullanıcılar buraya girer
                    if (!modelUser.getUUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) &&
                            modelUser.getToken().equals(me.getToken())) {

                        Logger.i("Add Item: " + modelUser.getEmail());

                        ModelUserTracker modelUserTracker = new ModelUserTracker();
                        modelUserTracker.setEmail(modelUser.getEmail());
                        modelUserTracker.setAd(modelUser.getAd());
                        modelUserTracker.setSoyAd(modelUser.getSoyAd());
                        modelUserTracker.setProfilePictureUrl("");

                        mAdapter.addItem(modelUserTracker);

                    }
                    */

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                return modelUserTrackers;
            }

            @Override
            public List<ModelUserTracker> get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
    }


    @DebugLog
    public static void deleteModelUser() {
        new Delete().from(ModelUser.class).execute();
    }

    @DebugLog
    public static void deleteModelUserTrackerList() {
        new Delete().from(ModelUserTracker.class).execute();
    }

}
