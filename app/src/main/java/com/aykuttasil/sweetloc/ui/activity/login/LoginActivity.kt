/* Author - Aykut Asil(aykuttasil) */
package com.aykuttasil.sweetloc.ui.activity.login

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aykuttasil.androidbasichelperlib.UiHelper
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.data.Resource
import com.aykuttasil.sweetloc.databinding.ActivityLoginLayoutBinding
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.activity.base.BaseActivity
import com.aykuttasil.sweetloc.ui.activity.main.MainActivity
import com.aykuttasil.sweetloc.util.extension.bind
import com.aykuttasil.sweetloc.util.extension.setupToolbar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login_layout.*
import javax.inject.Inject

open class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()

        loginViewModel = ViewModelProviders.of(this@LoginActivity, viewModelFactory).get(LoginViewModel::class.java)

        val binding = bind<ActivityLoginLayoutBinding>(R.layout.activity_login_layout).apply {
            lifecycleOwner = this@LoginActivity
            viewmodel = loginViewModel
        }

        loginViewModel.liveSnackbar.observe(this, Observer {
            Snackbar.make(view, it ?: "", Snackbar.LENGTH_LONG).show()
        })

        loginViewModel.liveOkDialog.observe(this, Observer {
            UiHelper.UiDialog.newInstance(this).getOKDialog(it.title, it.content, it.icon).show()
        })

        loginViewModel.liveLogin.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                Resource.Status.ERROR -> {
                    loginViewModel.liveOkDialog.value = it.errorData
                }
            }
        })
    }

    private fun initToolbar() {
        setupToolbar(R.id.toolbar) {
            title = "SweetLoc"
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
