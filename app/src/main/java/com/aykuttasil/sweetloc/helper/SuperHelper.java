package com.aykuttasil.sweetloc.helper;

import com.aykuttasil.sweetloc.db.DbManager;
import com.google.firebase.auth.FirebaseAuth;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 12.07.2016.
 */
public class SuperHelper extends com.aykuttasil.androidbasichelperlib.SuperHelper {

    @DebugLog
    public static void ResetSweetLoc() {
        logoutUser();
    }

    @DebugLog
    public static boolean checkUser() {
        return FirebaseAuth.getInstance().getCurrentUser() != null && DbManager.getModelUser() != null;
    }

    public static void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        DbManager.deleteModelUser();
    }
}
