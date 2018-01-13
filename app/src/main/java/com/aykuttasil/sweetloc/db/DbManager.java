package com.aykuttasil.sweetloc.db;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.aykuttasil.sweetloc.model.ModelSweetLocPreference;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.aykuttasil.sweetloc.model.ModelUserTracker;

import java.util.List;

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
    public static List<ModelUserTracker> getModelUserTracker() {
        return new Select().from(ModelUserTracker.class).execute();
    }

    @DebugLog
    public static String getOneSignalUserId() {
        ModelUser modelUser = new Select().from(ModelUser.class).executeSingle();

        if (modelUser.getOneSignalUserId() != null && !modelUser.getOneSignalUserId().isEmpty()) {
            return modelUser.getOneSignalUserId();
        } else {
            return null;
        }
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
