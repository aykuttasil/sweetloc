package com.aykuttasil.sweetloc.ui.activity.map

import android.Manifest
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NavUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import com.afollestad.materialdialogs.DialogAction
import com.aykuttasil.androidbasichelperlib.UiHelper
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.activity.base.BaseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.orhanobut.logger.Logger
import com.patloew.rxlocation.RxLocation
import com.tbruyelle.rxpermissions.RxPermissions
import hugo.weaving.DebugLog
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_maps.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


open class MapsActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var rxLocation: RxLocation

    val isMapReady: MutableLiveData<Boolean> = MutableLiveData()

    companion object {
        private val WAIT_LOCATION_SECOND = 30
    }

    lateinit var mapsViewModel: MapsViewModel

    private var isReceiveLocation = false

    private val mCompositeDisposible: CompositeDisposable = CompositeDisposable()
    var mGoogleMap: GoogleMap? = null
    var mapMarker: HashMap<Any, Any> = HashMap()
    var mSnackBarKonum: Snackbar? = null


    private val TURKEY = LatLngBounds(
            LatLng(36.299172, 26.248221), //Güney Batı
            LatLng(41.835412, 44.781357) //Kuzey Doğu
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mapsViewModel = ViewModelProviders.of(this, viewModelFactory).get(MapsViewModel::class.java)

        setup()

        isMapReady.observe(this, android.arch.lifecycle.Observer {
            if (it == true) {

            } else {

            }
        })

        FabMap.onClick { FabMapClick() }
    }

    private fun setup() {
        initToolbar()
        permissionControl()
    }

    private fun initToolbar() {
        setSupportActionBar(Toolbar)
        supportActionBar!!.title = "Harita"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_indigo_300_24dp)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    private fun permissionControl() {
        RxPermissions(this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe({ result ->
                    if (result!!) {
                        initMap()
                    } else {
                        val dialog = UiHelper.UiDialog.newInstance(this).getOKDialog("Uyarı", "Haritanın doğru çalışması için tüm izinleri vermelisiniz.", null)
                        dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener { _ -> permissionControl() }
                    }
                }, { error -> Logger.e(error, "HATA") })
    }

    private fun initMap() {
        (map as SupportMapFragment).getMapAsync(this)
        mapsViewModel.sendMyLocation().observe(this, android.arch.lifecycle.Observer {
            Logger.i("aa: " + it)
        })
        // initLocationListener()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Logger.i("Permission is not Granted !")
            return
        }

        this.mGoogleMap = googleMap

        mGoogleMap?.isMyLocationEnabled = true
        mGoogleMap?.uiSettings?.isZoomControlsEnabled = true
        mGoogleMap?.uiSettings?.isZoomGesturesEnabled = true

        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = (width * 0.12).toInt() // offset from edges of the map 12% of screen
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(TURKEY, width, height, padding))
        mGoogleMap?.setInfoWindowAdapter(this)
        mGoogleMap?.setOnInfoWindowClickListener(this)
        mGoogleMap?.isTrafficEnabled = true

        //setMap()
    }

    /*
    @SuppressLint("MissingPermission")
    @DebugLog
    private fun initLocationListener() {
        val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(30000)
                .setFastestInterval(5000)

        val locationSettingsRequest = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)
                .build()

        val disposable = rxLocation.settings()
                .checkAndHandleResolution(locationSettingsRequest)
                .flatMapObservable { granted ->
                    return@flatMapObservable if (granted) {
                        rxLocation.settings()
                                .checkAndHandleResolution(locationSettingsRequest)
                                .flatMapObservable({ rxLocation.sendMyLocation().updates(locationRequest) })
                    } else {
                        return@flatMapObservable Observable.error<Throwable>(Exception("Konum izni gerekli."))
                    }
                }
                .retry() // Eğer onError çalışırsa tekrar subscribe olunuyor
                .subscribeOn(Schedulers.io())
                .subscribe({
                    SuperHelper.sendLocationInformation(it)
                }, { error ->
                    Logger.e(error, "HATA")
                    //SuperHelper.CrashlyticsError(error)
                })

        mCompositeDisposible.add(disposable)
    }


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
        val marker = mGoogleMap?.addMarker(
                MarkerOptions()
                        .position(latLng)
                        .title(modelUser.email)

                //.icon(BitmapDescriptorFactory.fromBitmap())
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                //.title(title)
                //.snippet(snippet)
        )

        mapMarker.put(marker!!, modelLocation)
        mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 10.0f))
        marker.showInfoWindow()
    }

    @DebugLog
    private fun addMarker(modelUserTracker: ModelUserTracker, modelLocation: ModelLocation?) {
        isReceiveLocation = true
        if (mSnackBarKonum != null && mSnackBarKonum!!.isShown) {
            mSnackBarKonum!!.dismiss()
        }
        val latLng = LatLng(modelLocation!!.latitude, modelLocation.longitude)
        for ((key, value) in mapMarker) {
            try {
                val marker = key as Marker
                val sendMyLocation = value as ModelLocation
                if (marker.title == modelUserTracker.email) {
                    marker.remove()
                }
            } catch (e: Exception) {
                SuperHelper.CrashlyticsError(e)
                mGoogleMap?.clear()
                break
            }

        }

        val marker = mGoogleMap?.addMarker(
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

        mapMarker.put(marker!!, modelLocation)
        //mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 10.0f));
        //marker.showInfoWindow();
    }
     */

    /**
     * InfoWindow için custom view oluştururken ilk buraya firer.Eğer null dönerse getInfoContents e girer.
     *
     * @param marker
     * @return
     */
    @DebugLog
    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    /**
     * InfoWindow customview oluşturulması için bu fonksiyon düzenlenir..
     *
     * @param marker
     * @return
     */
    @DebugLog
    override fun getInfoContents(marker: Marker): View {
        val vi = LayoutInflater.from(this).inflate(R.layout.custom_infowindow_layout, null, false)

        /*try {
            val userMail = vi.findViewById<TextView>(R.id.TextView_UserMail)
            val userLocTime = vi.findViewById<TextView>(R.id.TextView_UserLocTime)
            val userLocAccuracy = vi.findViewById<TextView>(R.id.TextView_UserLocAccuracy)

            val modelLocation = mapMarker[marker] as ModelLocation

            userMail.text = Html.fromHtml("<b>Email: </b>" + marker.title)
            userLocTime.text = Html.fromHtml("<b>Zaman: </b>" + modelLocation.formatTime)
            userLocAccuracy.text = Html.fromHtml("<b>Sapma: </b>" + String.format(Locale.getDefault(), "%.2f", modelLocation.accuracy) + " m")
        } catch (e: Exception) {
            SuperHelper.CrashlyticsError(e)
            e.printStackTrace()
        }*/

        return vi
    }

    @DebugLog
    override fun onInfoWindowClick(marker: Marker) {
        val markerLatLng = marker.position
        mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 15.0f))
    }

    fun FabMapClick() {
        mSnackBarKonum = UiHelper.UiSnackBar.newInstance(Toolbar, "Son konumlar getiriliyor.\n" + "Lütfen bekleyiniz... ", Snackbar.LENGTH_INDEFINITE)
        mSnackBarKonum!!.show()
        isReceiveLocation = false
        mSnackBarKonum!!.view.postDelayed({
            // Eğer sendMyLocation bilgisi alınmış ise isReceiveLocation = true olur
            if (!isReceiveLocation) {
                UiHelper.UiSnackBar.showSimpleSnackBar(Toolbar, "Konum alınamadı!", Snackbar.LENGTH_LONG)
            }
        }, TimeUnit.SECONDS.toMillis(WAIT_LOCATION_SECOND.toLong()))

        //SuperHelper.sendNotif(Const.ACTION_KONUM_YOLLA)
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

    @DebugLog
    override fun onDestroy() {
        mCompositeDisposible.dispose()
        super.onDestroy()
    }

}

