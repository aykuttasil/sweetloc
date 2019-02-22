package com.aykuttasil.sweetloc.ui.activity.login

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import butterknife.ButterKnife
import butterknife.OnClick
import com.aykuttasil.androidbasichelperlib.UiHelper
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.activity.base.BaseActivity
import hugo.weaving.DebugLog
import kotlinx.android.synthetic.main.activity_login_layout.*
import javax.inject.Inject

/**
 * Created by aykutasil on 4.07.2016.
 */
open class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_layout)
        ButterKnife.bind(this)
        initToolbar()

        loginViewModel = ViewModelProviders.of(this@LoginActivity, viewModelFactory).get(LoginViewModel::class.java)

        loginViewModel.observableSnackBar.observe(this, Observer {
            Snackbar.make(view, it ?: "", Snackbar.LENGTH_LONG).show()
        })

        loginViewModel.observableOkDialog.observe(this, Observer {
            UiHelper.UiDialog.newInstance(this).getOKDialog(it?.title, it?.content, it?.icon).show()
        })
    }

    private fun initToolbar() {
        setSupportActionBar(Toolbar)
        supportActionBar!!.title = "SweetLoc"
    }


    @OnClick(R.id.Button_GirisYap)
    fun btnGirisYapClick() {
        if (!validateInput()) {
            return
        }

        loginViewModel.login(EditText_Email.text.toString(), EditText_Parola.text.toString()).observe(this, Observer {
            if (it != null) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })
    }

    @OnClick(R.id.Button_KayitOl)
    fun btnRegisterClick() {
        if (!validateInput()) {
            return
        }

        loginViewModel.register(EditText_Email.text.toString(), EditText_Parola.text.toString()).observe(this, Observer {
            if (it != null) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        })
    }

    @DebugLog
    private fun validateInput(): Boolean {
        return !com.aykuttasil.androidbasichelperlib.SuperHelper.validateIsEmpty(EditText_Email, EditText_Parola)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
