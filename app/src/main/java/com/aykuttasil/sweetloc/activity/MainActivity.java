package com.aykuttasil.sweetloc.activity;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.aykuttasil.sweetloc.model.event.FcmRegistraionIDEvent;
import com.aykuttasil.sweetloc.service.MyFirebaseInstanceIdService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 23.06.2016.
 */
@EActivity(R.layout.activity_main_layout)
public class MainActivity extends BaseActivity {

    public static final int LOGIN_REQUEST_CODE = 1001;

    DatabaseReference mDatabaseReferance;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @DebugLog
    @AfterViews
    public void MainActivityInit() {
        startFirebaseInstanceIDService();

        mDatabaseReferance = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Logger.d("onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Logger.d("onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        };

    }

    @DebugLog
    private void startFirebaseInstanceIDService() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Logger.i(token);
        if (token == null) {
            Intent intent = new Intent(this, MyFirebaseInstanceIdService.class);
            startService(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @DebugLog
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            UiHelper.UiDialog.newInstance(this).getOKDialog("Merhaha", user.getEmail(), null).show();
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity_.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }
    }

    @UiThread
    @DebugLog
    public void saveUser(ModelUser user) {
        mDatabaseReferance.child(ModelUser.class.getSimpleName()).child(user.getToken()).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    UiHelper.UiDialog.newInstance(MainActivity.this).getOKDialog("Database Error", databaseError.getMessage(), null).show();
                }
            }
        });
    }

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


    @DebugLog
    @OnActivityResult(LOGIN_REQUEST_CODE)
    public void ActivityResultLogin(int resultCode) {
        switch (resultCode) {
            case RESULT_OK: {

                break;
            }
        }
    }


    @DebugLog
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FcmRegistraionIDEvent event) {

    }

    @DebugLog
    @Override
    protected void onStop() {
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        super.onStop();
    }
}
