package com.aykuttasil.sweetloc.ui.activity.main

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.activity.base.BaseActivity
import com.aykuttasil.sweetloc.ui.activity.map.MapsActivity
import com.aykuttasil.sweetloc.ui.activity.profile.ProfileActivity
import com.aykuttasil.sweetloc.util.extension.setupToolbar
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.notificationManager
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
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            val intent = Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            startActivity(intent)
        }
    }

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

    override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
    ) {
        when (requestCode) {
            LOGIN_REQUEST_CODE -> when (resultCode) {
                Activity.RESULT_OK -> {
                    viewModel.isUserLogin.value = true
                }
            }
        }
    }
}
