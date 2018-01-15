package com.aykuttasil.sweetloc.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

import com.activeandroid.ActiveAndroid
import com.aykuttasil.sweetloc.BuildConfig
import com.aykuttasil.sweetloc.di.AppInjector
import com.aykuttasil.sweetloc.service.NotificationOpenedHandler
import com.aykuttasil.sweetloc.service.NotificationReceivedHandler
import com.crashlytics.android.Crashlytics
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.onesignal.OneSignal
import com.orhanobut.logger.LogLevel
import com.orhanobut.logger.Logger
import com.patloew.rxlocation.RxLocation
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector

import hugo.weaving.DebugLog
import io.fabric.sdk.android.Fabric
import javax.inject.Inject

/**
 * Created by aykutasil on 23.06.2016.
 */
open class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var rxLocation: RxLocation

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        // initializeActiveAndroid();
        initSweetLoc()
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }

    /*
    @DebugLog
    public void initializeActiveAndroid() {
        ActiveAndroid.initialize(new Configuration.Builder(getApplicationContext())
                .addModelClass(ModelUser.class)
                .addModelClass(ModelLocation.class)
                .addModelClass(ModelSweetLocPreference.class)
                .addModelClass(ModelUserTracker.class)
                .create());
    }
    */

    @DebugLog
    private fun initSweetLoc() {
        Logger.init("SweetLocLogger")
                .methodCount(3)
                .logLevel(if (BuildConfig.DEBUG) LogLevel.FULL else LogLevel.NONE)
                .methodOffset(0)

        Fabric.with(this, Crashlytics())

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.ERROR)
        //OneSignal.init(this, "535821025252", "283c0725-f1ae-434a-8ea5-09f61b1246fc", new NotificationOpenedHandler(), new NotificationReceivedHandler());

        OneSignal.startInit(this)
                .setNotificationReceivedHandler(NotificationReceivedHandler())
                .setNotificationOpenedHandler(NotificationOpenedHandler())
                //.autoPromptLocation(true)
                .init()

        FacebookSdk.sdkInitialize(applicationContext)

        AppEventsLogger.activateApp(this)
    }

    override fun attachBaseContext(base: Context) {
        try {
            super.attachBaseContext(base)
            MultiDex.install(this)
        } catch (ignored: RuntimeException) {
            // Multidex support doesn't play well with Robolectric yet
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        ActiveAndroid.dispose()
    }
}
