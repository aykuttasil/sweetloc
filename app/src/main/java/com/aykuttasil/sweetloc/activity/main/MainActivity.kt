package com.aykuttasil.sweetloc.activity.main

import android.annotation.TargetApi
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.activity.base.BaseActivity
import com.aykuttasil.sweetloc.activity.login.LoginActivity_
import com.aykuttasil.sweetloc.activity.map.MapsActivity_
import com.aykuttasil.sweetloc.activity.profile.ProfileActivity_
import com.aykuttasil.sweetloc.db.DbManager
import com.aykuttasil.sweetloc.fragment.UserTrackerListFragment_
import com.aykuttasil.sweetloc.helper.SuperHelper
import com.onesignal.OneSignal
import com.orhanobut.logger.Logger
import hugo.weaving.DebugLog
import kotlinx.android.synthetic.main.activity_main.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.OnActivityResult

/**
 * Created by aykutasil on 23.06.2016.
 */
@EActivity(R.layout.activity_main)
open class MainActivity : BaseActivity() {

    @DebugLog
    @AfterViews
    override fun initializeAfterViews() {
        initToolbar()
        updateUi()
    }

    @DebugLog
    override fun initToolbar() {
        setSupportActionBar(Toolbar)
        supportActionBar?.title = "SweetLoc"
    }

    @DebugLog
    override fun updateUi() {
        if (!SuperHelper.checkUser()) {
            SuperHelper.logoutUser()
            LoginActivity_.intent(this).startForResult(BaseActivity.LOGIN_REQUEST_CODE)
            // goLoginFacebookActivity(this);
        } else {
            initMain()
        }
    }

    @DebugLog
    private fun initMain() {
        SuperHelper.startPeriodicTask(this)
        checkAndOpenAudioSettings()

        if (DbManager.getOneSignalUserId() == null) {
            OneSignal.idsAvailable { userId, registrationId ->
                Logger.i("OneSignal userId: " + userId)
                Logger.i("OneSignal regId: " + registrationId)

                val modelUser = DbManager.getModelUser()
                modelUser.oneSignalUserId = userId
                modelUser.save()
                SuperHelper.updateUser(modelUser)

                goFragment()
            }
        } else {
            goFragment()
        }
    }

    @DebugLog
    private fun goFragment() {
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
                updateUi()
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun checkAndOpenAudioSettings() {
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !notificationManager.isNotificationPolicyAccessGranted) {
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
