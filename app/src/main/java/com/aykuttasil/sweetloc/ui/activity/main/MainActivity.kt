/* Author - Aykut Asil(@aykuttasil) */
package com.aykuttasil.sweetloc.ui.activity.main

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.activity.base.BaseActivity
import com.aykuttasil.sweetloc.ui.activity.map.MapsActivity
import com.aykuttasil.sweetloc.ui.activity.profile.ProfileActivity
import com.aykuttasil.sweetloc.ui.fragment.roommemberlist.RoomMemberListFragmentDirections
import com.aykuttasil.sweetloc.util.LocationLiveData
import com.aykuttasil.sweetloc.util.extension.setupToolbar
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

open class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val viewModel by viewModels<MainActivityViewModel> { viewModelFactory }

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("setContentView")
        setContentView(R.layout.activity_main)

        setupToolbar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            setDefaultDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "SweetLoc"
        }

        lifecycle.addObserver(viewModel)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        // NavigationUI.setupActionBarWithNavController(this, navController)
        NavigationUI.setupWithNavController(toolbar, navController)

        updateLocation()
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
        listenDynamicLink(intent)
    }

    private fun updateLocation() {
        runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
            LocationLiveData.create(
                this,
                25_000,
                5_000,
                onErrorCallback = object : LocationLiveData.OnErrorCallback {
                    override fun onLocationSettingsException(e: ApiException) {
                        e.printStackTrace()
                    }

                    override fun onPermissionsMissing() {
                        Toast.makeText(
                            this@MainActivity,
                            "Permission is required.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
                .observe(this, Observer { loc ->
                    viewModel.updateLocation(loc)
                })
        }
    }

    private fun listenDynamicLink(intent: Intent) {
        Timber.i("listenDynamicLink")

        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }

                // [START_EXCLUDE]
                // Display deep link in the UI
                if (deepLink != null) {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Found deep link!", Snackbar.LENGTH_LONG
                    ).show()

                    val roomName = deepLink.getQueryParameter("roomName")
                    val roomId = deepLink.lastPathSegment

                    Timber.d("roomId:$roomId roomName:$roomName")

                    val direction =
                        RoomMemberListFragmentDirections.actionGlobalRoomMemberListFragment(
                            roomId!!,
                            roomName!!,
                            true
                        )
                    navController.navigate(direction)

                    // val dest = navController.graph.findNode(R.id.roomMemberListFragment)
                    // dest?.addDeepLink("sweetloc/rooms/{roomId}?roomName={roomName}")

                    // navController.navigate(R.id.roomMemberListFragment,)
                    // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    // navController.handleDeepLink(intent)

                    // FirebaseAuth.getInstance().signInAnonymously()

                    /*
                    val bnd = Bundle().apply {
                        putString("roomName", deepLink.getQueryParameter("roomName"))
                        putString("roomId", deepLink.lastPathSegment)
                    }
                    */

                    /*
                    navController.createDeepLink()
                        .setArguments(bnd)
                        .setDestination(R.id.roomMemberListFragment)
                        .createTaskStackBuilder()
                        .startActivities()
                        */

                    // navController.handleDeepLink(intent)

                    // linkViewReceive.text = deepLink.toString()
                } else {
                    // Log.d(TAG, "getDynamicLink: no link found")
                }
                // [END_EXCLUDE]
            }
            .addOnFailureListener(this) { e ->
                e.printStackTrace()
                // Log.w(TAG, "getDynamicLink:onFailure", e)
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            listenDynamicLink(it)
        }
    }

    private fun initMain() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndOpenAudioSettings()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkAndOpenAudioSettings() {
        if (!(getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).isNotificationPolicyAccessGranted) {
            val intent =
                Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            startActivity(intent)
        }
    }

    /*
    fun x()
    {
        val pendingIntent = PendingIntent.getActivity(this,1,Intent(),PendingIntent.FLAG_ONE_SHOT)
        val fla = FusedLocationProviderClient(this)
        fla.requestLocationUpdates(LocationRequest(),pendingIntent)
    }
    */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // menuInflater.inflate(R.menu.menu_mainactivity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuProfil -> {
                startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                return true
            }
            R.id.menuMap -> {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOGIN_REQUEST_CODE -> when (resultCode) {
                Activity.RESULT_OK -> {
                    viewModel.isUserLogin.value = true
                }
            }
        }
    }
}
