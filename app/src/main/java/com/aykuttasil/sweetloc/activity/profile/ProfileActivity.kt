package com.aykuttasil.sweetloc.activity.profile

import android.content.Intent
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.DialogAction
import com.aykuttasil.androidbasichelperlib.UiHelper
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.activity.base.BaseActivity
import com.aykuttasil.sweetloc.activity.main.MainActivity_
import com.aykuttasil.sweetloc.helper.SuperHelper
import hugo.weaving.DebugLog
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity(R.layout.activity_profile)
open class ProfileActivity : BaseActivity() {

    @ViewById(R.id.Toolbar)
    lateinit var mToolbar: Toolbar
    //

    @DebugLog
    @AfterViews
    override fun initializeAfterViews() {
        initToolbar()
    }

    @DebugLog
    override fun initToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = "Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    @DebugLog
    override fun updateUi() {

    }

    @DebugLog
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionCikisYap -> {
                val dialog = UiHelper.UiDialog.newInstance(this).getOKCancelDialog("Çıkış Yap", "Devam edilsin mi?", null)
                dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener { view ->
                    dialog.dismiss()
                    SuperHelper.ResetSweetLoc(this)
                    // goLoginFacebookActivity(ProfileActivity.this);
                    // finish();
                    MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK).start()
                }
                dialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
