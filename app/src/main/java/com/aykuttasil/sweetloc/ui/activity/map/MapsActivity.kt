package com.aykuttasil.sweetloc.ui.activity.map

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.core.app.NavUtils
import androidx.lifecycle.ViewModelProviders
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.aykuttasil.androidbasichelperlib.UiHelper
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.activity.base.LoginBaseActivity
import com.aykuttasil.sweetloc.util.extension.setupToolbar
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.HashMap
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class MapsActivity : LoginBaseActivity(), OnMapReadyCallback, GoogleMap.InfoWindowAdapter,
    GoogleMap.OnInfoWindowClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val WAIT_LOCATION_SECOND = 30
    private val REQUEST_CHECK_SETTINGS: Int = 999

    private var isReceiveLocation = false

    private var _googleMap: GoogleMap? = null
    private var _mapMarker: HashMap<Any, Any> = HashMap()
    private var _snackBarKonum: Snackbar? = null
    private var _requestingLocationUpdates: Boolean = false

    private lateinit var _viewModel: MapsViewModel
    private lateinit var _fusedLocationProviderClient: FusedLocationProviderClient

    private val locationRequest: LocationRequest = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(30000)
        .setFastestInterval(5000)

    private val locationSettingsRequest: LocationSettingsRequest = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .setAlwaysShow(true)
        .build()

    private val TURKEY = LatLngBounds(
        LatLng(36.299172, 26.248221), //Güney Batı
        LatLng(41.835412, 44.781357) //Kuzey Doğu
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        setToolbar()

        _viewModel = ViewModelProviders.of(this, viewModelFactory).get(MapsViewModel::class.java)

        initMap()
        initLocationListener()
        _fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        FabMap.setOnClickListener { fabMapClick() }
    }

    override fun onStart() {
        super.onStart()
        if (_requestingLocationUpdates) startLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setToolbar() {
        setupToolbar(R.id.toolbar) {
            title = "Harita"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_indigo_300_24dp)
            setDisplayShowHomeEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    /*
    private fun permissionControl() {
        RxPermissions(this)
            .request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .subscribe({ result ->
                if (result!!) {
                    initMap()
                } else {
                    MaterialDialog(this)
                    val dialog = UiHelper.UiDialog.newInstance(this).getOKDialog(
                        "Uyarı",
                        "Haritanın doğru çalışması için tüm izinleri vermelisiniz.", null
                    )
                    dialog.getActionButton(WhichButton.POSITIVE)
                        .setOnClickListener { _ -> permissionControl() }
                }
            }, { error -> Timber.e(error, "HATA") })
    }
    */

    private fun initMap() {
        (map as SupportMapFragment).getMapAsync(this)

        /*
        _viewModel.sendMyLocation().observe(this, Observer {
            Timber.i("aa: $it")
        })
        */
        // initLocationListener()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        /*
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.i("Permission is not Granted !")
            return
        }
        */

        this._googleMap = googleMap

        // _googleMap?.isMyLocationEnabled = true
        _googleMap?.uiSettings?.isZoomControlsEnabled = true
        _googleMap?.uiSettings?.isZoomGesturesEnabled = true

        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = (width * 0.12).toInt() // offset from edges of the map 12% of screen
        _googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(TURKEY, width, height, padding))
        _googleMap?.setInfoWindowAdapter(this)
        _googleMap?.setOnInfoWindowClickListener(this)
        _googleMap?.isTrafficEnabled = true

        //setMap()
    }

    private fun initLocationListener() {
        LocationServices.getSettingsClient(this@MapsActivity).checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener {
                _requestingLocationUpdates = true
                startLocationUpdates()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        exception.startResolutionForResult(
                            this@MapsActivity,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }

        /*
        val disposable = rxLocation.settings()
            .checkAndHandleResolution(locationSettingsRequest)
            .flatMapObservable { granted ->
                return@flatMapObservable if (granted) {
                    rxLocation.settings()
                        .checkAndHandleResolution(locationSettingsRequest)
                        .flatMapObservable { rxLocation.sendMyLocation().updates(locationRequest) }
                } else {
                    return@flatMapObservable Observable.error<Throwable>(Exception("Konum izni gerekli."))
                }
            }
            .retry() // Eğer onError çalışırsa tekrar subscribe olunuyor
            .subscribeOn(Schedulers.io())
            .subscribe({
                SweetLocHelper.sendLocationInformation(it)
            }, { error ->
                Logger.e(error, "HATA")
                //SweetLocHelper.CrashlyticsError(error)
            })

        mCompositeDisposible.add(disposable)
        */
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return

            for (location in locationResult.locations) {
                // Update UI with location data
                // ...
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() =
        runWithPermissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION) {
            _googleMap?.isMyLocationEnabled = true

            _fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null /* Looper */
            )
        }

    private fun stopLocationUpdates() {
        _fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    /*
    @DebugLog
    private fun setMap() {
        val databaseReference = FirebaseDatabase.getInstance().reference

        for (modelUserTracker in DbManager.getModelUserTracker()) {
            databaseReference.child(ModelLocation::class.java.simpleName)
                    .child(modelUserTracker.uuid)
                    .limitToLast(1)
                    .addChildEventListener(object : ChildEventListener {

                        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                            val modelLocation = dataSnapshot.getValue(ModelLocation::class.java)
                            addMarker(modelUserTracker, modelLocation)
                        }

                        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                            val modelLocation = dataSnapshot.getValue(ModelLocation::class.java)
                            Logger.d(modelLocation)
                            addMarker(modelUserTracker, modelLocation)
                        }

                        override fun onChildRemoved(dataSnapshot: DataSnapshot) {

                        }

                        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

                        }

                        override fun onCancelled(databaseError: DatabaseError) {

                        }
                    })
        }
    }


    @DebugLog
    private fun addMarker(modelUser: ModelUser, modelLocation: ModelLocation) {
        val latLng = LatLng(modelLocation.latitude, modelLocation.longitude)
        val marker = _googleMap?.addMarker(
                MarkerOptions()
                        .position(latLng)
                        .title(modelUser.email)

                //.icon(BitmapDescriptorFactory.fromBitmap())
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                //.title(title)
                //.snippet(snippet)
        )

        _mapMarker.put(marker!!, modelLocation)
        _googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 10.0f))
        marker.showInfoWindow()
    }

    @DebugLog
    private fun addMarker(modelUserTracker: ModelUserTracker, modelLocation: ModelLocation?) {
        isReceiveLocation = true
        if (_snackBarKonum != null && _snackBarKonum!!.isShown) {
            _snackBarKonum!!.dismiss()
        }
        val latLng = LatLng(modelLocation!!.latitude, modelLocation.longitude)
        for ((key, value) in _mapMarker) {
            try {
                val marker = key as Marker
                val sendMyLocation = value as ModelLocation
                if (marker.title == modelUserTracker.email) {
                    marker.remove()
                }
            } catch (e: Exception) {
                SweetLocHelper.CrashlyticsError(e)
                _googleMap?.clear()
                break
            }

        }

        val marker = _googleMap?.addMarker(
                MarkerOptions()
                        .position(latLng)
                        .title(modelUserTracker.email)
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_check_light))
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                //.title(title)
                //.snippet(snippet)
        )
        val weakReference = WeakReference(marker)
        val target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                weakReference.get()?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_account_circle_light_blue_300_24dp))
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {

            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                weakReference.get()?.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
            }
        }

        Picasso.with(this)
                .load(modelUserTracker.profilePictureUrl)
                .transform(PicassoCircleTransform())
                .into(target)

        _mapMarker.put(marker!!, modelLocation)
        //_googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 10.0f));
        //marker.showInfoWindow();
    }
     */

    /**
     * InfoWindow için custom view oluştururken ilk buraya firer.Eğer null dönerse getInfoContents e girer.
     *
     * @param marker
     * @return
     */
    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    /**
     * InfoWindow customview oluşturulması için bu fonksiyon düzenlenir..
     *
     * @param marker
     * @return
     */
    override fun getInfoContents(marker: Marker): View {
        val vi = LayoutInflater.from(this).inflate(R.layout.custom_infowindow_layout, null, false)

        /*try {
            val userMail = vi.findViewById<TextView>(R.id.TextView_UserMail)
            val userLocTime = vi.findViewById<TextView>(R.id.TextView_UserLocTime)
            val userLocAccuracy = vi.findViewById<TextView>(R.id.TextView_UserLocAccuracy)

            val modelLocation = _mapMarker[marker] as ModelLocation

            userMail.text = Html.fromHtml("<b>Email: </b>" + marker.title)
            userLocTime.text = Html.fromHtml("<b>Zaman: </b>" + modelLocation.formatTime)
            userLocAccuracy.text = Html.fromHtml("<b>Sapma: </b>" + String.format(Locale.getDefault(), "%.2f", modelLocation.accuracy) + " m")
        } catch (e: Exception) {
            SweetLocHelper.CrashlyticsError(e)
            e.printStackTrace()
        }*/

        return vi
    }

    override fun onInfoWindowClick(marker: Marker) {
        val markerLatLng = marker.position
        _googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 15.0f))
    }

    fun fabMapClick() {
        _snackBarKonum = UiHelper.UiSnackBar.newInstance(
            toolbar,
            "Son konumlar getiriliyor.\n" + "Lütfen bekleyiniz... ", Snackbar.LENGTH_INDEFINITE
        )
        _snackBarKonum!!.show()
        isReceiveLocation = false
        _snackBarKonum!!.view.postDelayed({
            // Eğer sendMyLocation bilgisi alınmış ise isReceiveLocation = true olur
            if (!isReceiveLocation) {
                UiHelper.UiSnackBar.showSimpleSnackBar(
                    toolbar, "Konum alınamadı!",
                    Snackbar.LENGTH_LONG
                )
            }
        }, TimeUnit.SECONDS.toMillis(WAIT_LOCATION_SECOND.toLong()))

        //SweetLocHelper.sendNotif(Const.ACTION_KONUM_YOLLA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

}

