package com.aykuttasil.sweetloc.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.SystemClock;

import com.aykuttasil.sweetloc.BuildConfig;
import com.aykuttasil.sweetloc.app.Const;
import com.aykuttasil.sweetloc.db.DbManager;
import com.aykuttasil.sweetloc.model.ModelLocation;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.aykuttasil.sweetloc.model.ModelUserTracker;
import com.aykuttasil.sweetloc.receiver.SingleLocationRequestReceiver;
import com.crashlytics.android.Crashlytics;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 12.07.2016.
 */
public class SuperHelper extends com.aykuttasil.androidbasichelperlib.SuperHelper {

    @DebugLog
    public static void ResetSweetLoc(Context context) {
        DbManager.deleteModelUser();
        SuperHelper.stopPeriodicTask(context);
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

    @DebugLog
    public static void CrashlyticsError(Throwable error) {
        if (DbManager.getModelUser() != null && DbManager.getModelUser().getEmail() != null) {
            Crashlytics.setUserEmail(DbManager.getModelUser().getEmail());
            Crashlytics.logException(error);
        } else {
            Crashlytics.logException(error);
        }
    }

    @DebugLog
    public static void CrashlyticsLog(String log) {
        if (DbManager.getModelUser() != null && DbManager.getModelUser().getEmail() != null) {
            Crashlytics.setUserEmail(DbManager.getModelUser().getEmail());
            Crashlytics.log(log);
        } else {
            Crashlytics.log(log);
        }
    }


    public static void sendNotif(String action) {
        try {
            JSONObject mainObject = new JSONObject();

            JSONObject contents = new JSONObject();
            contents.put("en", "SweetLoc - Hello");
            contents.put("tr", "SweetLoc - Merhaba");
            mainObject.put("contents", contents);

            JSONObject headings = new JSONObject();
            headings.put("en", "SweetLoc - Title");
            headings.put("tr", "SweetLoc - Başlık");
            mainObject.put("headings", headings);


            JSONObject data = new JSONObject();
            data.put(Const.ACTION, action);
            mainObject.put("data", data);

            JSONArray playerIds = new JSONArray();
            for (ModelUserTracker modelUserTracker : DbManager.getModelUserTracker()) {
                if (modelUserTracker.getOneSignalUserId() != null) {
                    playerIds.put(modelUserTracker.getOneSignalUserId());
                }
            }

            if (BuildConfig.DEBUG) {
                //playerIds.put("428ef398-76d3-4ca9-ab4c-60d591879365");
                //playerIds.put("cebff33f-4274-49d1-b8ee-b1126325e169");
            }

            mainObject.put("include_player_ids", playerIds);
            Logger.json(mainObject.toString());
            if (playerIds.length() > 0) {
                OneSignal.postNotification(mainObject, new OneSignal.PostNotificationResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Logger.json(response.toString());
                    }

                    @Override
                    public void onFailure(JSONObject response) {
                        Logger.json(response.toString());
                    }
                });
            }
        } catch (Exception e) {
            Logger.e(e,"HATA");
        }
    }
}
