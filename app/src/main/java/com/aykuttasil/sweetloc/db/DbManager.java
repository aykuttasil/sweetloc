package com.aykuttasil.sweetloc.db;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.aykuttasil.sweetloc.model.ModelSweetLocPreference;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.google.firebase.auth.FirebaseAuth;

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
    public static void deleteModelUser() {
        new Delete().from(ModelUser.class).execute();
    }

}
