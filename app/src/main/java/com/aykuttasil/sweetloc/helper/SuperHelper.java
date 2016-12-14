package com.aykuttasil.sweetloc.helper;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.aykuttasil.sweetloc.db.DbManager;
import com.aykuttasil.sweetloc.model.ModelLocation;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.logger.Logger;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 12.07.2016.
 */
public class SuperHelper extends com.aykuttasil.androidbasichelperlib.SuperHelper {

    @DebugLog
    public static void ResetSweetLoc() {
        DbManager.deleteModelUser();
        logoutUser();
    }

    @DebugLog
    public static boolean checkUser() {
        return FirebaseAuth.getInstance().getCurrentUser() != null && DbManager.getModelUser() != null;
    }

    public static void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }

    public static void addFragment(AppCompatActivity activity, Fragment fragment, int containerViewId) {

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.saveFragmentInstanceState(fragment);
        fragmentManager.putFragment(null, fragment.getClass().getSimpleName(), fragment);

    }

    public static void getFragment(AppCompatActivity activity, Fragment fragment, int containerViewId) {

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.getFragment(null, fragment.getClass().getSimpleName());

        fragmentManager.saveFragmentInstanceState(fragment);
        fragmentManager.putFragment(null, fragment.getClass().getSimpleName(), fragment);

    }

    @DebugLog
    public static void updateUser(ModelUser modelUser) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            FirebaseDatabase.getInstance().getReference()
                    .child(ModelUser.class.getSimpleName())
                    .child(user.getUid())
                    .setValue(modelUser);
        }
    }

    @DebugLog
    public static void sendLocationInformation(Location location) {

        Logger.i("Location gönderildi");
        Logger.d(location);

        ModelLocation modelLocation = new ModelLocation();
        modelLocation.setLatitude(location.getLatitude());
        modelLocation.setLongitude(location.getLongitude());
        modelLocation.setAccuracy(location.getAccuracy());
        modelLocation.setAddress(location.getProvider());
        modelLocation.setTime(location.getTime());
        modelLocation.setFormatTime(com.aykuttasil.androidbasichelperlib.SuperHelper.getFormatTime(location.getTime()));
        modelLocation.setCreateDate(com.aykuttasil.androidbasichelperlib.SuperHelper.getFormatTime());

        FirebaseDatabase.getInstance().getReference()
                .child(ModelLocation.class.getSimpleName())
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .push()
                .setValue(modelLocation);
    }

}
