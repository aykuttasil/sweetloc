package com.aykuttasil.sweetloc.activity.main

import android.app.Activity
import android.app.NotificationManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.activity.base.BaseActivity
import com.aykuttasil.sweetloc.activity.login.LoginActivity
import com.aykuttasil.sweetloc.activity.map.MapsActivity
import com.aykuttasil.sweetloc.activity.profile.ProfileActivity
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.fragment.UserTrackerListFragment
import com.aykuttasil.sweetloc.helper.SuperHelper
import hugo.weaving.DebugLog
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

open class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var mainActivityViewModel: MainActivityViewModel

    @DebugLog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeAfterViews()
        mainActivityViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
        mainActivityViewModel.checkUserLogin().observe(this, Observer {
            if (it!!) {
                initMain()
            } else {
                startActivityForResult(Intent(this@MainActivity, LoginActivity::class.java), BaseActivity.LOGIN_REQUEST_CODE)
            }
        })
    }

    fun initializeAfterViews() {
        initToolbar()
    }

    fun initToolbar() {
        setSupportActionBar(Toolbar)
        supportActionBar?.title = "SweetLoc"
    }

    private fun initMain() {
        checkAndOpenAudioSettings()
        goUserTrackerListFragment()
    }

    private fun goUserTrackerListFragment() {
        SuperHelper.ReplaceFragmentBeginTransaction(
                this@MainActivity,
                UserTrackerListFragment(),
                R.id.Container,
                false)
    }

    private fun checkAndOpenAudioSettings() {
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted) {
            val intent = Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_mainactivity, menu)
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
        when (resultCode) {
            Activity.RESULT_OK -> {
                mainActivityViewModel.isUserLogin.value = true
            }
        }
    }
}
