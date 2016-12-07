package com.aykuttasil.sweetloc.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.aykuttasil.sweetloc.BuildConfig;
import com.aykuttasil.sweetloc.model.ModelLocation;
import com.aykuttasil.sweetloc.model.ModelSweetLocPreference;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.aykuttasil.sweetloc.service.NotificationOpenedHandler;
import com.aykuttasil.sweetloc.service.NotificationReceivedHandler;
import com.crashlytics.android.Crashlytics;
import com.onesignal.OneSignal;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import hugo.weaving.DebugLog;
import io.fabric.sdk.android.Fabric;

/**
 * Created by aykutasil on 23.06.2016.
 */
public class AppSweetLoc extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeActiveAndroid();
        initSweetLoc();
    }


    @DebugLog
    public void initializeActiveAndroid() {
        ActiveAndroid.initialize(new Configuration.Builder(getApplicationContext())
                .addModelClass(ModelUser.class)
                .addModelClass(ModelLocation.class)
                .addModelClass(ModelSweetLocPreference.class)
                .create());
    }

    @DebugLog
    public void initSweetLoc() {
        Logger.init("SweetLocLogger")                // default PRETTYLOGGER or use just init()
                .methodCount(3)                      // default 2
                .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)        // default LogLevel.FULL
                .methodOffset(0);                    // default 0
        //.hideThreadInfo()                          // default shown
        //.logAdapter(new AndroidLogAdapter());      //default AndroidLogAdapter

        Fabric.with(this, new Crashlytics());

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.ERROR);
        //OneSignal.init(this, "535821025252", "283c0725-f1ae-434a-8ea5-09f61b1246fc", new NotificationOpenedHandler(), new NotificationReceivedHandler());

        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new NotificationReceivedHandler())
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .autoPromptLocation(true)
                .init();

    }

    @Override
    protected void attachBaseContext(Context base) {
        try {
            super.attachBaseContext(base);
            MultiDex.install(this);
        } catch (RuntimeException ignored) {
            // Multidex support doesn't play well with Robolectric yet
        }
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
