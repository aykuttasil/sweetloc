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
        FirebaseAuth.getInstance().signOut();
        DbManager.deleteModelUser();
    }
}
