package com.aykuttasil.sweetloc.ui.activity.main

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.activity.base.BaseActivity
import com.aykuttasil.sweetloc.ui.activity.map.MapsActivity
import com.aykuttasil.sweetloc.ui.activity.profile.ProfileActivity
import com.aykuttasil.sweetloc.util.LocationLiveData
import com.aykuttasil.sweetloc.util.extension.setupToolbar
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

open class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            setDefaultDisplayHomeAsUpEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "SweetLoc"
        }


        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        NavigationUI.setupWithNavController(toolbar, navController)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
        lifecycle.addObserver(viewModel)


        LocationLiveData.create(
            this,
            5000L,
            5000L,
            onErrorCallback = object : LocationLiveData.OnErrorCallback {
                override fun onLocationSettingsException(e: ApiException) {
                    e.printStackTrace()
                }

                override fun onPermissionsMissing() {
                    Toast.makeText(this@MainActivity, "Permission is required.", Toast.LENGTH_LONG).show()
                }
            })
            .observe(this, Observer { loc ->
                viewModel.updateLocation(loc)
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    private fun initMain() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndOpenAudioSettings()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkAndOpenAudioSettings() {
        if (!(getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).isNotificationPolicyAccessGranted) {
            val intent = Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
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
            }
            R.id.menuMap -> {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
            }
        }
        return true
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
