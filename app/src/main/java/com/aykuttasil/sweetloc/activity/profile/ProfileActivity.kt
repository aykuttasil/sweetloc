package com.aykuttasil.sweetloc.activity.profile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.DialogAction
import com.aykuttasil.androidbasichelperlib.UiHelper
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.activity.base.BaseActivity
import com.aykuttasil.sweetloc.activity.main.MainActivity
import com.aykuttasil.sweetloc.helper.SuperHelper
import hugo.weaving.DebugLog
import kotlinx.android.synthetic.main.activity_profile.*

open class ProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initializeAfterViews()
    }

    fun initializeAfterViews() {
        initToolbar()
    }

    @DebugLog
    fun initToolbar() {
        setSupportActionBar(Toolbar)
        supportActionBar!!.title = "Profil"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
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

                    val intent = Intent(this@ProfileActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                dialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
