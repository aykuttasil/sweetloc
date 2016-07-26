package com.aykuttasil.sweetloc.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.aykuttasil.androidbasichelperlib.SuperHelper;
import com.aykuttasil.sweetloc.model.ModelLocation;
import com.aykuttasil.sweetloc.receiver.LocationReceiver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.EService;

import hugo.weaving.DebugLog;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

/**
 * Created by aykutasil on 4.07.2016.
 */
@EService
public class PeriodicService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static GoogleApiClient mGoogleApiClient;
    private Intent mIntent;

    @DebugLog
    @Nullable
    @Override
    public IBinder onBind(Intent ıntent) {
        return null;
    }

    @DebugLog
    @Override
    public void onCreate() {
        super.onCreate();
        buildAndConnectGoogleApiClient();
    }

    @DebugLog
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int id = intent.getIntExtra("android.support.content.wakelockid", 0);
        Logger.i("android.support.content.wakelockid : " + id);
        this.mIntent = intent;
        return START_REDELIVER_INTENT;
    }

    @DebugLog
    public synchronized void buildAndConnectGoogleApiClient() {

        Logger.i(mGoogleApiClient == null ? "null" : "not null");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        Logger.i(mGoogleApiClient.isConnected() ? "connected" : "not connected");
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        } else {
            init();
        }
    }

    @DebugLog
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        init();
    }

    @DebugLog
    @Override
    public void onConnectionSuspended(int i) {

    }

    @DebugLog
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @DebugLog
    private void init() {
        LocationRequest();
    }


    @DebugLog
    private void LocationRequest() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Logger.i("Manifest Permission not granted !");
            return;
        }

        FusedLocationApi.getLocationAvailability(mGoogleApiClient).isLocationAvailable();
        int LocationPeriod = 30;

        long intervalTime = LocationPeriod * 1000;
        long fastestTime = 10 * 1000;

        final LocationRequest locationRequestHighAccuracy = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(intervalTime)
                .setFastestInterval(fastestTime);

        final LocationRequest locationRequestPowerBalanced = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(intervalTime)
                .setFastestInterval(fastestTime);

        final LocationRequest locationRequestLowPower = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setInterval(intervalTime)
                .setFastestInterval(fastestTime);


        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequestHighAccuracy)
                .setAlwaysShow(true)
                .build();

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequest);

        result.setResultCallback(locationResult -> {

            LocationSettingsStates locationSettingsStates = locationResult.getLocationSettingsStates();

            if (locationSettingsStates.isGpsUsable()) {
                Logger.i("isGpsUsable");
                //FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequestHighAccuracy, this);
            } else {
                Logger.i("isLocationUsable");
                //FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequestLowPower, this);
            }

            /*
            else if (locationSettingsStates.isNetworkLocationUsable()) {
                Logger.i("isNetworkLocationUsable");
                //FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequestPowerBalanced, this);
            } else if (locationSettingsStates.isLocationUsable()) {
                Logger.i("isLocationUsable");
                //FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequestLowPower, this);
            }
            */
        });
    }

    @DebugLog
    @Override
    public void onLocationChanged(Location location) {
        Logger.d(location);

        ModelLocation modelLocation = new ModelLocation();
        modelLocation.setLatitude(location.getLatitude());
        modelLocation.setLongitude(location.getLongitude());
        modelLocation.setAccuracy(location.getAccuracy());
        modelLocation.setAddress(location.getProvider());
        modelLocation.setTime(location.getTime());
        modelLocation.setFormatTime(SuperHelper.getFormatTime(location.getTime()));

        FirebaseDatabase.getInstance().getReference()
                .child(ModelLocation.class.getSimpleName())
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .push()
                .setValue(modelLocation);

        FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        LocationReceiver.completeWakefulIntent(mIntent);
        PeriodicService_.intent(this).stop();
    }

    @DebugLog
    @Override
    public void onDestroy() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
        super.onDestroy();
    }
}
