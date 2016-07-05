package com.aykuttasil.sweetloc.activity;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.aykuttasil.sweetloc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 4.07.2016.
 */
@EActivity(R.layout.activity_login_layout)
public class LoginActivity extends BaseActivity {

    @ViewById(R.id.EditText_Parola)
    EditText mEditText_Parola;

    @ViewById(R.id.EditText_Email)
    EditText mEditText_Email;
    //

    @DebugLog
    @AfterViews
    public void LoginActivityInit() {
    }

    @DebugLog
    @Click(R.id.Button_GirisYap)
    public void Button_GirisYapClick() {


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mEditText_Email.getText().toString(),
                mEditText_Parola.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    UiHelper.UiDialog.newInstance(LoginActivity.this).getOKDialog("Hata", task.getException().getMessage(), null).show();
                }
            }
        });

    }


}
