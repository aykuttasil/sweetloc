package com.aykuttasil.sweetloc.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.Fragment;

import com.aykuttasil.sweetloc.BuildConfig;
import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.app.Const;
import com.aykuttasil.sweetloc.receiver.LocationReceiver;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.orhanobut.logger.Logger;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 8.07.2016.
 */
public abstract class BaseFragment extends Fragment {

    abstract void initializeAfterViews();

    FirebaseRemoteConfig firebaseRemoteConfig;

    @DebugLog
    public void setPeriodicTask(Context context) {

        long cacheExpiration = 3600; // 1 hour in seconds.

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();

        firebaseRemoteConfig.setConfigSettings(configSettings);

        firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        if (firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        firebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(
                complete -> {
                    Logger.i("Remote Config Fetch complete.");
                    if (complete.isSuccessful()) {
                        Logger.i("Remote Config Fetch is succesful.");
                        firebaseRemoteConfig.activateFetched();
                        Logger.i("Remote Config activated..");
                        setAlarm(context);

                    } else {
                        Logger.i("Remote Config Fetch is not succesful.");
                    }
                });

        //long periodicTime = FirebaseRemoteConfig.getInstance().getLong("periodic_time") * 1000;
        //setAlarm(context, periodicTime);

    }

    @DebugLog
    private void setAlarm(Context context) {

        long periodicTime = firebaseRemoteConfig.getLong("periodic_time") * 1000;
        Logger.i("Remote Config Periodic Time: " + periodicTime);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context.getApplicationContext(), LocationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                Const.REQUEST_CODE_BROADCAST_LOCATION,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        // setInexactRepeating() kullanıldığında sabit aralıklar verilmek zorundadır. Uygulamalar genelde bu şekilde periodic
        // servislerini çalıştırır. Android, düzenlenmiş diğer işlemlerle aynı anda periodic servisi çalıştıracağından
        // pil ömrü uzar.
        // -----
        // Eğer kendimiz zaman belirlemek istiyorsak (her 10 saniyede bir çalıştır gibi) setRepeating() kullanmamamız gerekir.
        // -----
        // Alarmı hemen çalıştırmaya başla ve her 15 dakikada bir tekrarla.
        /*alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.currentThreadTimeMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pendingIntent);*/

        //alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP);


        // AlarmManager.ELAPSED_REALTIME -> ELAPSED_REALTIME system başlangıcını baz alır. Eğer gerçek saat ile ilgili bir işlem yapılmıcak
        // ise bu parametre kullanılmalıdır.
        // RTC -> Cihazın local saatini baz alır. Örneğin perşembe saat 4 de yapılcak bi iş belirlemek istersen RTC kullanmalıyız.
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 3000,
                periodicTime,
                pendingIntent);
    }


    @DebugLog
    public void stopPeriodicTask(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, LocationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                Const.REQUEST_CODE_BROADCAST_LOCATION,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

}
