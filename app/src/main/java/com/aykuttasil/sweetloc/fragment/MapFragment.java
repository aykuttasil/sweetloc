package com.aykuttasil.sweetloc.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.activity.MainActivity;
import com.aykuttasil.sweetloc.db.DbManager;
import com.aykuttasil.sweetloc.model.ModelLocation;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import java.util.HashMap;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 11.07.2016.
 */
@EFragment(R.layout.fragment_map_layout)
public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    View mView;
    Context mContext;
    MainActivity mActivity;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;
    HashMap<Object, Object> mapMarker = new HashMap();
    //
    private LatLngBounds TURKEY = new LatLngBounds(
            new LatLng(36.299172, 26.248221),//Güney Batı
            new LatLng(41.835412, 44.781357) //Kuzey Doğu
    );

    @DebugLog
    public static MapFragment newInstance() {
        MapFragment rotalamaHaritaFragment = new MapFragment_();
        return rotalamaHaritaFragment;
    }

    @DebugLog
    @AfterViews
    public void MapFragmentInit() {
        setHasOptionsMenu(true);
        this.mContext = getContext();
        this.mActivity = (MainActivity) getActivity();

        stopPeriodicTask(mContext);
        setPeriodicTask(mContext);
        setMapInit();
    }


    @DebugLog
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return null;
    }

    @DebugLog
    @Override
    public void onResume() {
        super.onResume();
    }

    @DebugLog
    private void setMapInit() {
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @DebugLog
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Logger.i("Permissinon is not Granted !");
            return;
        }
        this.mGoogleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(TURKEY, 0));
        googleMap.setInfoWindowAdapter(this);

        setMap();
    }

    @DebugLog
    private void setMap() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        Query queryUser = databaseReference.child(ModelUser.class.getSimpleName());

        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ModelUser currentUser = DbManager.getModelUser();
                //Logger.d(currentUser);
                //Logger.i(currentUser.getEmail());
                //Logger.i(currentUser.getToken());

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModelUser modelUser = dataSnapshot1.getValue(ModelUser.class);
                    //Logger.d(modelUser.getEmail());

                    //Logger.i(modelUser.getUUID());
                    //Logger.i(modelUser.getToken());
                    //Logger.i(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    //Logger.i(currentUser.getEmail());
                    //Logger.i(currentUser.getToken());


                    // Aynı token a sahip diğer kullancılar buraya girer
                    if (!modelUser.getUUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) &&
                            modelUser.getToken().equals(DbManager.getModelUser().getToken())) {

                        Logger.i(modelUser.getEmail() + " konum dinleniyor.");

                        databaseReference.child(ModelLocation.class.getSimpleName())
                                .child(modelUser.getUUID())
                                .limitToLast(2)
                                .addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        ModelLocation modelLocation = dataSnapshot.getValue(ModelLocation.class);
                                        Logger.d(modelLocation);

                                        addMarker(modelUser, modelLocation);
                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                        ModelLocation modelLocation = dataSnapshot.getValue(ModelLocation.class);
                                        Logger.d(modelLocation);

                                        addMarker(modelUser, modelLocation);
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
    @DebugLog
    public void onEventMainThread(List<ModelRotaPoint> list) {
        modelRotaPointList = list;
        setMarker();
        //setPolyLine();

    }
    */

    @DebugLog
    private void addMarker(ModelUser modelUser, ModelLocation modelLocation) {
        LatLng latLng = new LatLng(modelLocation.getLatitude(), modelLocation.getLongitude());

        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(modelUser.getEmail())
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_check_light))
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                //.title(title)
                //.snippet(snippet)
        );
        mapMarker.put(marker, modelLocation);

    }

    @DebugLog
    private void setPolyLine() {
        /*
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(-18.142, 178.431), 2));

        // Polylines are useful for marking paths and routes on the map.
        mGoogleMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(-33.866, 151.195))  // Sydney
                .add(new LatLng(-18.142, 178.431))  // Fiji
                .add(new LatLng(21.291, -157.821))  // Hawaii
                .add(new LatLng(37.423, -122.091))  // Mountain View
        );
        */

        /*
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.geodesic(true);
        for (int a = 0; a < modelRotaPointList.size(); a++) //ModelRotaPoint modelRotaPoint : modelRotaPointList) {
        {
            polylineOptions.add(new LatLng(Double.parseDouble(modelRotaPointList.get(a).getEnlem()),
                    Double.parseDouble(modelRotaPointList.get(a).getBoylam())));
        }
        mGoogleMap.addPolyline(polylineOptions);
        */
    }

    @DebugLog
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @DebugLog
    @Override
    public View getInfoContents(Marker marker) {
        View vi = LayoutInflater.from(mContext).inflate(R.layout.custom_infowindow_layout, null, false);
        TextView userMail = (TextView) vi.findViewById(R.id.TextView_UserMail);
        TextView userLocTime = (TextView) vi.findViewById(R.id.TextView_UserLocTime);
        TextView userLocAccuracy = (TextView) vi.findViewById(R.id.TextView_UserLocAccuracy);

        ModelLocation modelLocation = (ModelLocation) mapMarker.get(marker);

        userMail.setText(marker.getTitle());
        userLocTime.setText(modelLocation.getFormatTime());
        userLocAccuracy.setText(String.valueOf(modelLocation.getAccuracy()));
        return vi;
    }

    @DebugLog
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        switch (item.getItemId()) {
            case R.id.: {
                //ActionGonderiler();
                break;
            }
        }
        */
        return false;
    }


    @DebugLog
    @Override
    public void onPause() {
        super.onPause();
    }
}