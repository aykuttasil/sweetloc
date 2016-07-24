package com.aykuttasil.sweetloc.app;

import com.facebook.stetho.Stetho;

/**
 * Created by aykutasil on 24.07.2016.
 */
public class AppSweetLocDebug extends AppSweetLoc {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
