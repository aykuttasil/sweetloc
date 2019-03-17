package com.aykuttasil.sweetloc

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.aykuttasil.sweetloc.BuildConfig
import com.aykuttasil.sweetloc.di.AppInjector
import com.aykuttasil.sweetloc.service.NotificationOpenedHandler
import com.aykuttasil.sweetloc.service.NotificationReceivedHandler
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.onesignal.OneSignal
import com.orhanobut.logger.LogLevel
import com.orhanobut.logger.Logger
import com.patloew.rxlocation.RxLocation
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
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
        initSweetLoc()
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }

    private fun initSweetLoc() {
        Logger.init("SweetLocLogger")
                .methodCount(3)
                .logLevel(if (BuildConfig.DEBUG) LogLevel.FULL else LogLevel.NONE)
                .methodOffset(0)


        initializeFabric()

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

    private fun initializeFabric() {
        val crashlyticsCore = CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build()

        val crashlytics = Crashlytics.Builder()
                .core(crashlyticsCore)
                .build()

        Fabric.with(this, crashlytics)
    }

    override fun attachBaseContext(base: Context) {
        try {
            super.attachBaseContext(base)
            MultiDex.install(this)
        } catch (ignored: RuntimeException) {
            // Multidex support doesn't play well with Robolectric yet
        }
    }
}
