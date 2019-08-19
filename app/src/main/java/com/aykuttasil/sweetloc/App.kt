/* Author - Aykut Asil(@aykuttasil) */
package com.aykuttasil.sweetloc

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.aykuttasil.sweetloc.di.AppInjector
import com.aykuttasil.sweetloc.service.NotificationOpenedHandler
import com.aykuttasil.sweetloc.service.NotificationReceivedHandler
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.onesignal.OneSignal
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.vondear.rxtool.RxTool
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.fabric.sdk.android.Fabric
import timber.log.Timber
import javax.inject.Inject

open class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        initSweetLoc()
    }

    private fun initSweetLoc() {
        initLog()
        initFabric()
        initOneSignal()

        // FacebookSdk.sdkInitialize(applicationContext)
        // AppEventsLogger.activateApp(this)

        RxTool.init(this)
    }

    private fun initOneSignal() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.ERROR)
        // OneSignal.init(this, "535821025252", "283c0725-f1ae-434a-8ea5-09f61b1246fc", new NotificationOpenedHandler(), new NotificationReceivedHandler());

        OneSignal.startInit(this)
            .setNotificationReceivedHandler(NotificationReceivedHandler())
            .setNotificationOpenedHandler(NotificationOpenedHandler())
            // .autoPromptLocation(true)
            .init()
    }

    private fun initLog() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true) // (Optional) Whether to show thread info or not. Default true
            .methodCount(2) // (Optional) How many method line to show. Default 2
            .methodOffset(5) // (Optional) Hides internal method calls up to offset. Default 5
            // .logStrategy(customLog)      // (Optional) Changes the log strategy to print out. Default LogCat
            .tag("SweetlocLogger") // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })

        // Timber.plant(Timber.DebugTree())
        // Set methodOffset to 5 in order to hide internal method calls
        Timber.plant(object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                Logger.log(priority, tag, message, t)
            }
        })
    }

    private fun initFabric() {
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
        } catch (e: RuntimeException) {
        }
    }
}
