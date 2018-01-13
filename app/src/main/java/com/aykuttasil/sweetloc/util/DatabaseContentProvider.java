package com.aykuttasil.sweetloc.util;

import com.activeandroid.Configuration;
import com.activeandroid.content.ContentProvider;
import com.aykuttasil.sweetloc.model.ModelLocation;
import com.aykuttasil.sweetloc.model.ModelSweetLocPreference;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.aykuttasil.sweetloc.model.ModelUserTracker;

/**
 * Created by aykutasil on 14.01.2018.
 */

public class DatabaseContentProvider extends ContentProvider {

    @Override
    protected Configuration getConfiguration() {
        Configuration.Builder builder = new Configuration.Builder(getContext());
        builder.addModelClass(ModelUser.class)
                .addModelClass(ModelLocation.class)
                .addModelClass(ModelSweetLocPreference.class)
                .addModelClass(ModelUserTracker.class);

        return builder.create();
    }
}