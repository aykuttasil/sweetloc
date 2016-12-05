package com.aykuttasil.sweetloc.activity;

import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.helper.SuperHelper;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

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

    @ViewById(R.id.EditText_Token)
    EditText mEditText_Token;

    @ViewById(R.id.EditText_ImageUrl)
    EditText mEditText_ImageUrl;

    @ViewById(R.id.Toolbar)
    Toolbar mToolbar;
    //

    @DebugLog
    @AfterViews
    @Override
    void initializeAfterViews() {
        initToolbar();
    }

    @DebugLog
    @Override
    void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("SweetLoc ...");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_indigo_300_24dp);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @DebugLog
    @Click(R.id.Button_GirisYap)
    public void Button_GirisYapClick() {


        if (!validateInput()) {
            return;
        }


        FirebaseAuth.getInstance().signInWithEmailAndPassword(mEditText_Email.getText().toString(),
                mEditText_Parola.getText().toString())
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        ModelUser modelUser = new ModelUser();
                        FirebaseUser user = task.getResult().getUser();

                        modelUser.setUUID(user.getUid());
                        modelUser.setEmail(user.getEmail());
                        modelUser.setParola(user.getDisplayName());
                        modelUser.setToken(mEditText_Token.getText().toString());
                        modelUser.setImageUrl(mEditText_ImageUrl.getText().toString().isEmpty() ? null : mEditText_ImageUrl.getText().toString());
                        modelUser.save();

                        setResult(RESULT_OK);
                        finish();

                    } else {
                        UiHelper.UiDialog.newInstance(LoginActivity.this).getOKDialog("Kullanıcı Bulunamadı", task.getException().getMessage(), null).show();
                    }

                });

    }

    @DebugLog
    @Click(R.id.Button_KayitOl)
    public void Button_KayitOlClick() {

        if (!validateInput()) {
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mEditText_Email.getText().toString(),
                mEditText_Parola.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ModelUser modelUser = new ModelUser();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                modelUser.setUUID(user.getUid());
                modelUser.setEmail(user.getEmail());
                modelUser.setParola(user.getDisplayName());
                modelUser.setToken(mEditText_Token.getText().toString());
                modelUser.setImageUrl(mEditText_ImageUrl.getText().toString().isEmpty() ? null : mEditText_ImageUrl.getText().toString());
                modelUser.save();


                // -> /ModelUser/Klksdfkjhsdöfnsdmfn/{}
                FirebaseDatabase.getInstance().getReference()
                        .child(ModelUser.class.getSimpleName())
                        .child(user.getUid())
                        .setValue(modelUser);

                setResult(RESULT_OK);
                finish();
            } else {
                UiHelper.UiDialog.newInstance(LoginActivity.this).getOKDialog("Uyarı", task.getException().getMessage(), null).show();
            }
        });
    }

    @DebugLog
    private boolean validateInput() {
        return !SuperHelper.validateIsEmpty(mEditText_Email, mEditText_Parola, mEditText_Token);
    }
}
