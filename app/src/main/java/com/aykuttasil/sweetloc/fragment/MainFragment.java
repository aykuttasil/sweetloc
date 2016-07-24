package com.aykuttasil.sweetloc.fragment;

import android.content.Context;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.activity.MainActivity;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 8.07.2016.
 */
@EFragment(R.layout.fragment_main_layout)
public class MainFragment extends BaseFragment {

    //
    Context mContext;
    MainActivity mActivity;
    DatabaseReference mDatabaseReferance;


    @DebugLog
    @AfterViews
    public void MainFragmentInit() {
        this.mContext = getContext();
        mActivity = (MainActivity) getActivity();
        mDatabaseReferance = FirebaseDatabase.getInstance().getReference();
        //
        mActivity.SetNavigationHeader(mActivity);
        setPeriodicTask(mContext);
    }

    @UiThread
    @DebugLog
    public void saveUser(ModelUser user) {
        mDatabaseReferance.child(ModelUser.class.getSimpleName()).push().setValue(user);

        /*
        mDatabaseReferance.child(ModelUser.class.getSimpleName()).child(user.getToken()).setValue(user, new DatabaseReference.CompletionListener() {
            @DebugLog
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    UiHelper.UiDialog.newInstance(MainActivity.this).getOKDialog("Database Error", databaseError.getMessage(), null).show();
                }
            }
        });
        */
    }


    /*
    @DebugLog
    @Click(R.id.Button_SaveData)
    public void Button_SaveDataClick() {
        ModelUser modelUser = new ModelUser();
        modelUser.setAd("Aykut");
        modelUser.setSoyAd("Asil");
        modelUser.setRegID(FirebaseInstanceId.getInstance().getToken());
        modelUser.setEmail("aykuttasil@hotmail.com");
        modelUser.setTelefon("053581512442342");
        modelUser.setToken(String.valueOf(new Random().nextInt()));

        saveUser(modelUser);
    }
    */

    /*
    @DebugLog
    @Click(R.id.Button_HaritaAc)
    public void Button_HaritaAcClick() {
        SuperHelper.ReplaceFragmentBeginTransaction(mActivity,
                MapFragment.newInstance(),
                MainActivity.FRAGMENT_CONTAINER_ID,
                MapFragment.class.getSimpleName(),
                false);
    }
    */
}
