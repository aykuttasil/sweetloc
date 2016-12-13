package com.aykuttasil.sweetloc.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.aykuttasil.sweetloc.BuildConfig;
import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.app.Const;
import com.aykuttasil.sweetloc.receiver.LocationReceiver;
import com.aykuttasil.sweetloc.service.MyFirebaseInstanceIdService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.orhanobut.logger.Logger;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 23.06.2016.
 */
public abstract class BaseActivity extends AppCompatActivity {


    abstract void initializeAfterViews();

    abstract void initToolbar();

    abstract void updateUi();

    public static final int LOGIN_REQUEST_CODE = 1001;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    @DebugLog
    public void goLoginFacebookActivity(AppCompatActivity activity) {
        //activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        LoginFacebookActivity_.intent(activity).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK).start();
    }

    @DebugLog
    public void startFirebaseInstanceIDService() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Logger.i("Token:" + token);
        if (token == null) {
            Logger.i("in -> startFirebaseInstanceIDService , token = null");
            Intent intent = new Intent(this, MyFirebaseInstanceIdService.class);
            startService(intent);
        }
    }

    @DebugLog
    public void SetNavigationHeader(MainActivity activity) {
        //NavigationView navHeader = (NavigationView) activity.findViewById(R.id.nav_view);
        //TextView email = (TextView) navHeader.getHeaderView(0).findViewById(R.id.navigationEmail);

        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //email.setText(user.getEmail());
    }


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
