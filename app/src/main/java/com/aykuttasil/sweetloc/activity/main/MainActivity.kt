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
import com.aykuttasil.sweetloc.activity.login.LoginActivity_
import com.aykuttasil.sweetloc.activity.map.MapsActivity_
import com.aykuttasil.sweetloc.activity.profile.ProfileActivity_
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.fragment.UserTrackerListFragment_
import com.aykuttasil.sweetloc.helper.SuperHelper
import hugo.weaving.DebugLog
import kotlinx.android.synthetic.main.activity_main.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.OnActivityResult
import javax.inject.Inject

/**
 * Created by aykutasil on 23.06.2016.
 */
@EActivity(R.layout.activity_main)
open class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var mainActivityViewModel: MainActivityViewModel

    @DebugLog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
        mainActivityViewModel.checkUserLogin().observe(this, Observer {
            if (it!!) {
                initMain()
            } else {
                LoginActivity_.intent(this).startForResult(BaseActivity.LOGIN_REQUEST_CODE)
            }
        })
    }

    @DebugLog
    @AfterViews
    override fun initializeAfterViews() {
        initToolbar()
    }

    @DebugLog
    override fun initToolbar() {
        setSupportActionBar(Toolbar)
        supportActionBar?.title = "SweetLoc"
    }

    @DebugLog
    override fun updateUi() {

    }

    @DebugLog
    private fun initMain() {
        checkAndOpenAudioSettings()
        goUserTrackerListFragment()
    }

    @DebugLog
    private fun goUserTrackerListFragment() {
        SuperHelper.ReplaceFragmentBeginTransaction(
                this@MainActivity,
                UserTrackerListFragment_.builder().build(),
                R.id.Container,
                false)
    }

    @DebugLog
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        updateUi()
    }

    @DebugLog
    @OnActivityResult(BaseActivity.LOGIN_REQUEST_CODE)
    fun activityResultLogin(resultCode: Int) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                mainActivityViewModel.isUserLogin.value = true
            }
        }
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

    @DebugLog
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuProfil -> {
                ProfileActivity_.intent(this).start()
            }
            R.id.menuMap -> {
                MapsActivity_.intent(this).start()
            }
        }
        return true
    }
}
