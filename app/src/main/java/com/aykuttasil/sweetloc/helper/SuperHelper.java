package com.aykuttasil.sweetloc.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.SystemClock;

import com.aykuttasil.sweetloc.app.Const;
import com.aykuttasil.sweetloc.db.DbManager;
import com.aykuttasil.sweetloc.model.ModelLocation;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.aykuttasil.sweetloc.receiver.SingleLocationRequestReceiver;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.logger.Logger;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 12.07.2016.
 */
public class SuperHelper extends com.aykuttasil.androidbasichelperlib.SuperHelper {

    @DebugLog
    public static void ResetSweetLoc() {
        DbManager.deleteModelUser();
        logoutUser();
    }

    @DebugLog
    public static boolean checkUser() {
        return FirebaseAuth.getInstance().getCurrentUser() != null && DbManager.getModelUser() != null;
    }

    @DebugLog
    public static void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }

    @DebugLog
    public static void updateUser(ModelUser modelUser) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            FirebaseDatabase.getInstance().getReference()
                    .child(ModelUser.class.getSimpleName())
                    .child(user.getUid())
                    .setValue(modelUser);
        }
    }

    @DebugLog
    public static void sendLocationInformation(Location location) {

        Logger.i("Location gönderildi");
        Logger.d(location);

        ModelLocation modelLocation = new ModelLocation();
        modelLocation.setLatitude(location.getLatitude());
        modelLocation.setLongitude(location.getLongitude());
        modelLocation.setAccuracy(location.getAccuracy());
        modelLocation.setAddress(location.getProvider());
        modelLocation.setTime(location.getTime());
        modelLocation.setFormatTime(com.aykuttasil.androidbasichelperlib.SuperHelper.getFormatTime(location.getTime()));
        modelLocation.setCreateDate(com.aykuttasil.androidbasichelperlib.SuperHelper.getFormatTime());

        FirebaseDatabase.getInstance().getReference()
                .child(ModelLocation.class.getSimpleName())
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .push()
                .setValue(modelLocation);
    }

    @DebugLog
    public static void startPeriodicTask(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context.getApplicationContext(), SingleLocationRequestReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                Const.REQUEST_CODE_BROADCAST_LOCATION,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 3000,
                AlarmManager.INTERVAL_HALF_HOUR,
                pendingIntent);
    }

    @DebugLog
    public static void stopPeriodicTask(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context.getApplicationContext(), SingleLocationRequestReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                Const.REQUEST_CODE_BROADCAST_LOCATION,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

}
