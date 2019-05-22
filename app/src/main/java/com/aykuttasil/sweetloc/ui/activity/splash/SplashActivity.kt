package com.aykuttasil.sweetloc.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.ui.activity.base.BaseActivity
import com.aykuttasil.sweetloc.ui.activity.login.LoginActivity
import com.aykuttasil.sweetloc.ui.activity.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        launch {
            delay(2000L)

            val isLogin = userRepository.checkUser()
            if (isLogin) {
                val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
                mainIntent.data = intent.data
                // mainIntent.putExtras(intent)
                // intent.extras?.let { mainIntent.putExtras(it) }
                startActivity(mainIntent)
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
            finish()
        }
    }
}