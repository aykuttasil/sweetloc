/* Author - Aykut Asil(aykuttasil) */
package com.aykuttasil.sweetloc.ui.activity.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aykuttasil.sweetloc.ui.dialog.ProgressDialogFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector, CoroutineScope {

    companion object {
        const val LOGIN_REQUEST_CODE = 1001
    }

    var job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val progressDialog: ProgressDialogFragment by lazy {
        ProgressDialogFragment().apply {
            isCancelable = false
        }
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    fun showProgressDialog() {
        if (!progressDialog.isVisible) {
            progressDialog.show(supportFragmentManager, "ProgressDialog")
        }
    }

    fun dismissProgressDialog() {
        if (progressDialog.isVisible) {
            progressDialog.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgressDialog()
        job.cancel()
    }

    //    @DebugLog
    //    public void setPeriodicTask(Context context) {
    //
    //        long cacheExpiration = 3600; // 1 hour in seconds.
    //
    //        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    //
    //        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
    //                .setDeveloperModeEnabled(BuildConfig.DEBUG)
    //                .build();
    //
    //        firebaseRemoteConfig.setConfigSettings(configSettings);
    //
    //        firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
    //
    //        if (firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
    //            cacheExpiration = 0;
    //        }
    //
    //        firebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(
    //                complete -> {
    //                    Logger.i("Remote Config Fetch complete.");
    //                    if (complete.isSuccessful()) {
    //                        Logger.i("Remote Config Fetch is succesful.");
    //                        firebaseRemoteConfig.activateFetched();
    //                        Logger.i("Remote Config activated..");
    //                        setAlarm(context);
    //
    //                    } else {
    //                        Logger.i("Remote Config Fetch is not succesful.");
    //                    }
    //                });
    //
    //        //long periodicTime = FirebaseRemoteConfig.getInstance().getLong("periodic_time") * 1000;
    //        //setAlarm(context, periodicTime);
    //
    //    }
    //
    //    @DebugLog
    //    private void setAlarm(Context context) {
    //
    //        long periodicTime = firebaseRemoteConfig.getLong("periodic_time") * 1000;
    //        Logger.i("Remote Config Periodic Time: " + periodicTime);
    //
    //        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    //
    //        Intent intent = new Intent(context.getApplicationContext(), LocationReceiver.class);
    //
    //        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
    //                Const.REQUEST_CODE_BROADCAST_LOCATION,
    //                intent,
    //                PendingIntent.FLAG_UPDATE_CURRENT);
    //
    //
    //        // setInexactRepeating() kullanıldığında sabit aralıklar verilmek zorundadır. Uygulamalar genelde bu şekilde periodic
    //        // servislerini çalıştırır. Android, düzenlenmiş diğer işlemlerle aynı anda periodic servisi çalıştıracağından
    //        // pil ömrü uzar.
    //        // -----
    //        // Eğer kendimiz zaman belirlemek istiyorsak (her 10 saniyede bir çalıştır gibi) setRepeating() kullanmamamız gerekir.
    //        // -----
    //        // Alarmı hemen çalıştırmaya başla ve her 15 dakikada bir tekrarla.
    //        /*alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
    //                SystemClock.currentThreadTimeMillis(),
    //                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
    //                pendingIntent);*/
    //
    //        //alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP);
    //
    //
    //        // AlarmManager.ELAPSED_REALTIME -> ELAPSED_REALTIME system başlangıcını baz alır. Eğer gerçek saat ile ilgili bir işlem yapılmayacak
    //        // ise bu parametre kullanılmalıdır.
    //        // RTC -> Cihazın local saatini baz alır. Örneğin perşembe saat 4 de yapılacak bir iş belirlemek istersek RTC kullanmalıyız.
    //        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
    //                SystemClock.elapsedRealtime() + 3000,
    //                periodicTime,
    //                pendingIntent);
    //
    //        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
    //                AlarmManager.INTERVAL_HALF_HOUR,
    //                periodicTime,
    //                pendingIntent);
    //    }
    //
    //
    //    @DebugLog
    //    public void stopPeriodicTask(Context context) {
    //        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    //        Intent intent = new Intent(context, LocationReceiver.class);
    //        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
    //                Const.REQUEST_CODE_BROADCAST_LOCATION,
    //                intent,
    //                PendingIntent.FLAG_UPDATE_CURRENT);
    //
    //        alarmManager.cancel(pendingIntent);
    //    }
}
