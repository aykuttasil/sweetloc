package com.aykuttasil.sweetloc.ui.activity.profile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.aykuttasil.androidbasichelperlib.UiHelper
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.helper.SweetLocHelper
import com.aykuttasil.sweetloc.ui.activity.base.LoginBaseActivity
import com.aykuttasil.sweetloc.ui.activity.main.MainActivity
import kotlinx.android.synthetic.main.activity_profile.*
import javax.inject.Inject

open class ProfileActivity : LoginBaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var helper: SweetLocHelper

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)

        setup()
    }

    private fun setup() {
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Profil"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionCikisYap -> {
                val dialog = UiHelper.UiDialog.newInstance(this)
                        .getOKCancelDialog("Çıkış Yap", "Devam edilsin mi?", null)

                dialog.positiveButton {
                    dialog.dismiss()
                    helper.resetSweetLoc(this)
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
