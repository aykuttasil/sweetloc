package com.aykuttasil.sweetloc.ui.activity.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.app.NavUtils
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import com.aykuttasil.androidbasichelperlib.UiHelper
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.data.LocationEntity
import com.aykuttasil.sweetloc.data.UserModel
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.activity.base.BaseActivity
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_maps.*
import timber.log.Timber
import java.util.HashMap
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class MapsActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.InfoWindowAdapter,
    GoogleMap.OnInfoWindowClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val _viewModel by viewModels<MapsViewModel> { viewModelFactory }

    private val args: MapsActivityArgs by navArgs()

    private val WAIT_LOCATION_SECOND = 30
    private val REQUEST_CHECK_SETTINGS: Int = 999

    private var isReceiveLocation = false

    private var _googleMap: GoogleMap? = null
    private var _mapMarker: HashMap<Marker, LocationEntity> = HashMap()
    private var _snackBarKonum: Snackbar? = null
    private var _requestingLocationUpdates: Boolean = false

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

        initMap()
        initLocationListener()
        _fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        _viewModel.getRoomMembersLocation(args.roomId).observe(this, Observer { roomLoc ->
            addMarker(roomLoc.user!!, roomLoc.location)
            Timber.d(roomLoc.user?.userEmail)
        })

        FabMap.setOnClickListener { fabMapClick() }
    }

    override fun onStart() {
        super.onStart()
        if (_requestingLocationUpdates) startLocationUpdates()
    }

    private fun setToolbar() {
        setupToolbar(R.id.toolbar) {
            title = "Üye Konumları"
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
    }

    override fun onMapReady(googleMap: GoogleMap) {
        _googleMap = googleMap

        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = (width * 0.12).toInt() // offset from edges of the map 12% of screen

        _googleMap?.apply {
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isZoomGesturesEnabled = true
            moveCamera(CameraUpdateFactory.newLatLngBounds(TURKEY, width, height, padding))
            setInfoWindowAdapter(this@MapsActivity)
            setOnInfoWindowClickListener(this@MapsActivity)
            isTrafficEnabled = true
        }

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

    private var locationCallback: LocationCallback? = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return

            for (location in locationResult.locations) {
                _viewModel.updateLocation(location)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        //runWithPermissions(Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION){
        _googleMap?.isMyLocationEnabled = true

        _fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
        //}
    }

    private fun stopLocationUpdates() {
        Timber.d("stopLocationUpdates")
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


    private fun addMarker(user: UserEntity, location: ModelLocation) {
        val latLng = LatLng(location.latitude, location.longitude)
        val marker = _googleMap?.addMarker(
                MarkerOptions()
                        .position(latLng)
                        .title(user.email)

                //.icon(BitmapDescriptorFactory.fromBitmap())
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                //.title(title)
                //.snippet(snippet)
        )

        _mapMarker[marker!!] = location
        _googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 10.0f))
        marker.showInfoWindow()
    }
    */


    private fun addMarker(user: UserModel, location: LocationEntity?) {
        val latLng = LatLng(location!!.latitude!!, location.longitude!!)
        for ((marker, loc) in _mapMarker) {
            try {
                if (marker.title == user.userEmail) {
                    marker.remove()
                }
            } catch (e: Exception) {
                _googleMap?.clear()
                break
            }

        }

        _googleMap?.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(user.userEmail)
                .snippet(user.userName)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            // .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_account_circle_light_blue_300_24dp))
        )?.also { marker ->
            _mapMarker[marker] = location
        }


        /*
        val weakReference = WeakReference(marker)
        val target = object : Target() {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                weakReference.get()
                    ?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_account_circle_light_blue_300_24dp))
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
            */

        // _mapMarker[marker!!] = location
        //_googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 10.0f));
        //marker.showInfoWindow();
    }


    /**
     * InfoWindow için custom view oluştururken ilk buraya girer. Eğer null dönerse getInfoContents'e girer.
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
        try {
            val userMail = vi.findViewById<TextView>(R.id.TextView_UserMail)
            val userLocTime = vi.findViewById<TextView>(R.id.TextView_UserLocTime)
            val userLocAccuracy = vi.findViewById<TextView>(R.id.TextView_UserLocAccuracy)

            val modelLocation = _mapMarker[marker] as LocationEntity

            userMail.text = Html.fromHtml("<b>Email: </b>" + marker.title)
            userLocTime.text = Html.fromHtml("<b>Zaman: </b>" + modelLocation.formatTime)
            userLocAccuracy.text = Html.fromHtml(
                "<b>Sapma: </b>" + String.format(
                    Locale.getDefault(),
                    "%.2f",
                    modelLocation.accuracy
                ) + " m"
            )
        } catch (e: Exception) {
            // SweetLocHelper.CrashlyticsError(e)
            e.printStackTrace()
        }

        return vi
    }

    override fun onInfoWindowClick(marker: Marker) {
        _googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15.0f))
    }

    private fun fabMapClick() {
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                if (resultCode == Activity.RESULT_OK) {
                    startLocationUpdates()
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
        locationCallback = null
    }
}

