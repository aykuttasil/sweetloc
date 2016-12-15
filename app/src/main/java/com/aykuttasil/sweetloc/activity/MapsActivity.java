package com.aykuttasil.sweetloc.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.aykuttasil.sweetloc.BuildConfig;
import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.app.Const;
import com.aykuttasil.sweetloc.db.DbManager;
import com.aykuttasil.sweetloc.helper.SuperHelper;
import com.aykuttasil.sweetloc.model.ModelLocation;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.aykuttasil.sweetloc.model.ModelUserTracker;
import com.aykuttasil.sweetloc.util.PicassoCircleTransform;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.onesignal.OneSignal;
import com.orhanobut.logger.Logger;
import com.patloew.rxlocation.RxLocation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import hugo.weaving.DebugLog;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

@EActivity(R.layout.activity_maps)
public class MapsActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    @FragmentById(R.id.map)
    SupportMapFragment mMapFragment;

    @ViewById(R.id.Toolbar)
    Toolbar mToolbar;

    //

    CompositeDisposable mCompositeDisposible;
    GoogleMap mGoogleMap;
    HashMap<Object, Object> mapMarker = new HashMap();

    private LatLngBounds TURKEY = new LatLngBounds(
            new LatLng(36.299172, 26.248221),//Güney Batı
            new LatLng(41.835412, 44.781357) //Kuzey Doğu
    );


    @DebugLog
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeDisposible = new CompositeDisposable();
    }

    @DebugLog
    @AfterViews
    public void initializeAfterViews() {
        initToolbar();
        permissionControl();
    }

    @DebugLog
    @Override
    void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Harita");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_indigo_300_24dp);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    void updateUi() {
    }

    @DebugLog
    private void permissionControl() {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(result -> {
                    if (result) {

                        initMap();

                    } else {
                        MaterialDialog dialog = UiHelper.UiDialog.newInstance(this).getOKDialog("Uyarı", "Haritanın doğru çalışması için tüm izinleri vermelisiniz.", null);
                        dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(view -> {
                            permissionControl();
                        });
                    }
                }, error -> {
                    Logger.e(error, "HATA");
                });
    }

    @DebugLog
    private void initMap() {

        mMapFragment.getMapAsync(this);

        initLocationListener();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Logger.i("Permissinon is not Granted !");
            return;
        }

        this.mGoogleMap = googleMap;

        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(TURKEY, width, height, padding));

        mGoogleMap.setInfoWindowAdapter(this);
        mGoogleMap.setOnInfoWindowClickListener(this);
        mGoogleMap.setTrafficEnabled(true);

        setMap();

    }

    @DebugLog
    private void initLocationListener() {

        RxLocation rxLocation = new RxLocation(this);

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(30000)
                .setFastestInterval(5000);

        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)
                .build();


        Disposable disposable = rxLocation.settings().checkAndHandleResolution(locationSettingsRequest)
                .flatMapObservable(new Function<Boolean, ObservableSource<Location>>() {
                    @DebugLog
                    @Override
                    public ObservableSource<Location> apply(Boolean granted) throws Exception {

                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return Observable.error(new Exception("Permission is not granted"));
                        }

                        if (granted) {
                            return rxLocation.location().updates(locationRequest);
                        } else {
                            return Observable.error(new Exception("GPS ayarlarında hata var."));
                        }
                    }
                })
                .retry()
                .subscribe(location -> {
                    SuperHelper.sendLocationInformation(location);
                });

        mCompositeDisposible.add(disposable);


                /*
                .flatMapMaybe(new Function<Location, MaybeSource<Address>>() {
                    @DebugLog
                    @Override
                    public MaybeSource<Address> apply(Location location) throws Exception {

                        SuperHelper.sendLocationInformation(location);

                        return rxLocation.geocoding().fromLocation(location);
                    }
                })
                .filter(address -> address.getCountryName() != null && !address.getCountryName().isEmpty())
                .subscribe(result -> {


                    //UiHelper.UiSnackBar.showSimpleSnackBar(mToolbar, result.getCountryName(), Snackbar.LENGTH_SHORT);
                    //Toast.makeText(MapsActivity.this, result.getCountryName(), Toast.LENGTH_LONG).show();
                }, error -> {
                    Logger.e(error, "HATA");
                });
                */
    }

    @DebugLog
    private void setMap() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        for (ModelUserTracker modelUserTracker : DbManager.getModelUserTracker()) {

            databaseReference.child(ModelLocation.class.getSimpleName())
                    .child(modelUserTracker.getUUID())
                    .limitToLast(2)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            Logger.i(s);
                            Logger.i(new Gson().toJson(dataSnapshot.getValue()));

                            ModelLocation modelLocation = dataSnapshot.getValue(ModelLocation.class);

                            addMarker(modelUserTracker, modelLocation);

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            ModelLocation modelLocation = dataSnapshot.getValue(ModelLocation.class);

                            Logger.d(modelLocation);

                            addMarker(modelUserTracker, modelLocation);
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }
    }

    @DebugLog
    private void addMarker(ModelUser modelUser, ModelLocation modelLocation) {

        LatLng latLng = new LatLng(modelLocation.getLatitude(), modelLocation.getLongitude());

        Marker marker = mGoogleMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .title(modelUser.getEmail())

                //.icon(BitmapDescriptorFactory.fromBitmap())
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                //.title(title)
                //.snippet(snippet)
        );

        mapMarker.put(marker, modelLocation);

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 10.0f));

        marker.showInfoWindow();
    }

    @DebugLog
    private void addMarker(ModelUserTracker modelUserTracker, ModelLocation modelLocation) {

        LatLng latLng = new LatLng(modelLocation.getLatitude(), modelLocation.getLongitude());

        Marker marker = mGoogleMap.addMarker(
                new MarkerOptions()
                        .position(latLng)
                        .title(modelUserTracker.getEmail())
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_check_light))
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                //.title(title)
                //.snippet(snippet)
        );

        WeakReference<Marker> weakReference = new WeakReference<>(marker);

        Target target = new Target() {

            @DebugLog
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                weakReference.get().setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
            }

            @DebugLog
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @DebugLog
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                weakReference.get().setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_account_circle_light_blue_300_24dp));
            }
        };

        Picasso.with(this)
                .load(modelUserTracker.getProfilePictureUrl())
                .transform(new PicassoCircleTransform())
                .into(target);

        mapMarker.put(marker, modelLocation);

        //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 10.0f));

        //marker.showInfoWindow();
    }

    /**
     * InfoWindow için custom view oluştururken ilk buraya firer.Eğer null dönerse getInfoContents e girer.
     *
     * @param marker
     * @return
     */
    @DebugLog
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /**
     * InfoWindow customview oluşturulması için bu fonksiyon düzenlenir..
     *
     * @param marker
     * @return
     */
    @DebugLog
    @Override
    public View getInfoContents(Marker marker) {

        View vi = LayoutInflater.from(this).inflate(R.layout.custom_infowindow_layout, null, false);

        try {

            TextView userMail = (TextView) vi.findViewById(R.id.TextView_UserMail);
            TextView userLocTime = (TextView) vi.findViewById(R.id.TextView_UserLocTime);
            TextView userLocAccuracy = (TextView) vi.findViewById(R.id.TextView_UserLocAccuracy);

            ModelLocation modelLocation = (ModelLocation) mapMarker.get(marker);

            userMail.setText(Html.fromHtml("<b>Email: </b>" + marker.getTitle()));
            userLocTime.setText(Html.fromHtml("<b>Zaman: </b>" + modelLocation.getFormatTime()));
            userLocAccuracy.setText(Html.fromHtml("<b>Sapma: </b>" + Double.toString(modelLocation.getAccuracy()) + " m"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return vi;
    }

    @DebugLog
    @Override
    public void onInfoWindowClick(Marker marker) {
        LatLng markerLatLng = marker.getPosition();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 15.0f));
    }

    @DebugLog
    @Click(R.id.FabMap)
    public void FabMapClick() {

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
            data.put(Const.ACTION, Const.ACTION_KONUM_YOLLA);
            mainObject.put("data", data);

            JSONArray playerIds = new JSONArray();
            for (ModelUserTracker modelUserTracker : DbManager.getModelUserTracker()) {
                if (modelUserTracker.getOneSignalUserId() != null) {
                    playerIds.put(modelUserTracker.getOneSignalUserId());
                }
            }

            if (BuildConfig.DEBUG) {
                playerIds.put("428ef398-76d3-4ca9-ab4c-60d591879365");
                playerIds.put("cebff33f-4274-49d1-b8ee-b1126325e169");
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

        } catch (JSONException e) {
            Logger.e(e, "HATA");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @DebugLog
    @Override
    protected void onDestroy() {
        mCompositeDisposible.dispose();
        super.onDestroy();
    }
}
