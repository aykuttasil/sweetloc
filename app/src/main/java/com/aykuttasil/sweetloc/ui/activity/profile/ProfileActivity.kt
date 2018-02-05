package com.aykuttasil.sweetloc.ui.activity.profile

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.DialogAction
import com.aykuttasil.androidbasichelperlib.UiHelper
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.helper.SuperHelper
import com.aykuttasil.sweetloc.ui.activity.base.BaseActivity
import com.aykuttasil.sweetloc.ui.activity.main.MainActivity
import hugo.weaving.DebugLog
import kotlinx.android.synthetic.main.activity_profile.*
import javax.inject.Inject

open class ProfileActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var superHelper: SuperHelper

    lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)

        setup()
    }

    private fun setup() {
        initToolbar()
    }

    @DebugLog
    private fun initToolbar() {
        setSupportActionBar(Toolbar)
        supportActionBar?.title = "Profil"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
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
                dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener { _ ->
                    dialog.dismiss()

                    superHelper.resetSweetLoc(this)

                    val intent = Intent(this@ProfileActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                dialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
