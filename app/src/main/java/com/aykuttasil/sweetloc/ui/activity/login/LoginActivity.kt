package com.aykuttasil.sweetloc.ui.activity.login

import android.app.Activity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import com.aykuttasil.androidbasichelperlib.UiHelper
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.databinding.ActivityLoginLayoutBinding
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.activity.base.BaseActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login_layout.*
import javax.inject.Inject

open class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProviders.of(this@LoginActivity, viewModelFactory)
            .get(LoginViewModel::class.java)

        val binding = DataBindingUtil.setContentView<ActivityLoginLayoutBinding>(this,
            R.layout.activity_login_layout)
        binding.lifecycleOwner = this
        binding.viewmodel = loginViewModel

        initToolbar()

        loginViewModel.liveSnackbar.observe(this, Observer {
            Snackbar.make(view, it ?: "", Snackbar.LENGTH_LONG).show()
        })

        loginViewModel.liveOkDialog.observe(this, Observer {
            UiHelper.UiDialog.newInstance(this)
                .getOKDialog(it.title, it.content, it.icon).show()
        })

        loginViewModel.liveUiStates.observe(this, Observer { states ->
            when (states) {
                is LoginUiStateSuccessfulLogin -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                is LoginUiStateSuccessfulRegister -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                is LoginUiStateError -> {
                    loginViewModel.liveOkDialog.value = states.dataOkDialog
                }
            }
        })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "SweetLoc"
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
