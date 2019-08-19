/* Author - Aykut Asil(@aykuttasil) */
package com.aykuttasil.sweetloc.ui.activity.base

/*
import android.content.Intent
import android.os.Bundle
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.ui.activity.login.LoginActivity
import kotlinx.coroutines.launch
import javax.inject.Inject


abstract class LoginBaseActivity : BaseActivity() {

    @Inject
    lateinit var userRepository: UserRepository

    var isLogin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch {
            isLogin = userRepository.checkUser()
            if (!isLogin) {
                startActivity(Intent(this@LoginBaseActivity.applicationContext, LoginActivity::class.java))
            }
        }
    }
}
*/
