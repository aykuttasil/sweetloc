package com.aykuttasil.sweetloc.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.aykuttasil.sweetloc.db.DbManager;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

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


}
