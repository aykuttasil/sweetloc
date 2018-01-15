package com.aykuttasil.sweetloc.activity.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.aykuttasil.androidbasichelperlib.UiHelper
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.activity.base.BaseActivity
import com.aykuttasil.sweetloc.helper.SuperHelper
import com.aykuttasil.sweetloc.model.ModelUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import hugo.weaving.DebugLog
import kotlinx.android.synthetic.main.activity_login_layout.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by aykutasil on 4.07.2016.
 */
open class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_layout)
        initToolbar()

        Button_GirisYap.onClick { btnGirisYapClick() }
        Button_KayitOl.onClick { btnRegisterClick() }

    }

    fun initToolbar() {
        setSupportActionBar(Toolbar)
        supportActionBar!!.title = "SweetLoc"
    }


    fun btnGirisYapClick() {
        if (!validateInput()) {
            return
        }

        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(
                        EditText_Email.text.toString(),
                        EditText_Parola.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        takeSweetLocToken(task.result.user)
                    } else {
                        UiHelper.UiDialog.newInstance(this@LoginActivity).getOKDialog("Kullanıcı Bulunamadı", task.exception!!.message, null).show()
                    }
                }
    }

    fun btnRegisterClick() {
        if (!validateInput()) {
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(EditText_Email.text.toString(),
                EditText_Parola.text.toString()).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                takeSweetLocToken(task.result.user)
            } else {
                UiHelper.UiDialog.newInstance(this@LoginActivity).getOKDialog("Uyarı", task.exception!!.message, null).show()
            }
        }
    }

    @DebugLog
    private fun validateInput(): Boolean {
        return !SuperHelper.validateIsEmpty(EditText_Email, EditText_Parola)
    }

    private fun takeSweetLocToken(user: FirebaseUser) {
        val tokenView = LayoutInflater.from(this).inflate(R.layout.custom_view_give_token_layout, null)
        val dialog = MaterialDialog.Builder(this)
                .title("SweetLoc İzleme Anahtarı")
                .customView(tokenView, true)
                .cancelable(false)
                .build()

        tokenView.findViewById<View>(R.id.DevamEt).setOnClickListener { _ ->
            val tokentext = tokenView.findViewById<EditText>(R.id.EditTextTrackerId)
            if (!SuperHelper.validateIsEmpty(tokentext)) {
                dialog.dismiss()
                val modelUser = ModelUser()
                modelUser.uuid = user.uid
                modelUser.email = user.email
                modelUser.parola = user.providerId
                modelUser.token = tokentext.text.toString()
                modelUser.imageUrl = user.photoUrl?.toString()
                modelUser.save()

                FirebaseDatabase.getInstance().reference
                        .child(ModelUser::class.java.simpleName)
                        .child(user.uid)
                        .setValue(modelUser)

                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        dialog.show()
    }


    @DebugLog
    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}