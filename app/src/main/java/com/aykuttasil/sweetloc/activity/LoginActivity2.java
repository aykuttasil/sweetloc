package com.aykuttasil.sweetloc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.helper.SuperHelper;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 7.12.2016.
 */

@EActivity(R.layout.activity_login2)
public class LoginActivity2 extends BaseActivity {

    @ViewById(R.id.LoginButton)
    LoginButton mLoginButton;

    //

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moveTaskToBack(false);
        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        //
        initAuthListener();
    }

    @DebugLog
    private void initAuthListener() {

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @DebugLog
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Logger.d("onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Logger.d("onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @DebugLog
    @AfterViews
    @Override
    void initializeAfterViews() {
        initToolbar();
        initFacebookLoginButton();
    }

    @DebugLog
    @Override
    void initToolbar() {

    }

    @DebugLog
    @Override
    void updateUi() {

    }

    @DebugLog
    private void initFacebookLoginButton() {

        mLoginButton.setReadPermissions("email", "public_profile");

        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @DebugLog
            @Override
            public void onSuccess(LoginResult loginResult) {
                Logger.d("facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @DebugLog
            @Override
            public void onCancel() {
                Logger.d("facebook:onCancel");
            }

            @DebugLog
            @Override
            public void onError(FacebookException error) {
                Logger.e(error, "facebook:onError");
            }
        });
    }

    @DebugLog
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @DebugLog
    public void handleFacebookAccessToken(AccessToken token) {

        Logger.d("handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @DebugLog
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Logger.d("signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Logger.w("signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity2.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            takeSweetLocToken(task.getResult().getUser());
                        }
                    }
                });
    }


    private void takeSweetLocToken(FirebaseUser user) {

        View tokenView = LayoutInflater.from(this).inflate(R.layout.custom_view_give_token_layout, null);

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("SweetLoc İzleme Anahtarı")
                .customView(tokenView, true)
                .cancelable(false)
                .build();

        tokenView.findViewById(R.id.DevamEt).setOnClickListener(view -> {


            EditText tokentext = ((EditText) tokenView.findViewById(R.id.EditTextTrackerId));

            if (!SuperHelper.validateIsEmpty(tokentext)) {

                dialog.dismiss();

                ModelUser modelUser = new ModelUser();
                modelUser.setUUID(user.getUid());
                modelUser.setEmail(user.getEmail());
                modelUser.setParola(user.getDisplayName());
                modelUser.setToken(tokentext.getText().toString());
                modelUser.save();

                setResult(RESULT_OK);
                finish();
            }
        });

        dialog.show();


    }

    @DebugLog
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    @DebugLog
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @DebugLog
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
