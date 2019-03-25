package com.aykuttasil.sweetloc.ui.activity.base

import android.content.Intent
import android.os.Bundle
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.ui.activity.login.LoginActivity
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

abstract class LoginBaseActivity : BaseActivity() {

    @Inject
    lateinit var userRepository: UserRepository

    var isLogin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            isLogin = userRepository.checkUser()
            if (!isLogin) {
                startActivityForResult(
                    Intent(this@LoginBaseActivity, LoginActivity::class.java),
                    BaseActivity.LOGIN_REQUEST_CODE
                )
            }
        }
    }
}