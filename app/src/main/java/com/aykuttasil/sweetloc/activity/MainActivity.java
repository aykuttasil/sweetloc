package com.aykuttasil.sweetloc.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.fragment.NavFragment_;
import com.aykuttasil.sweetloc.helper.SuperHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 23.06.2016.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewById(R.id.Toolbar)
    Toolbar mToolbar;

    @ViewById(R.id.Container)
    FrameLayout mContainer;
    //
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener mAuthListener;

    @DebugLog
    @AfterViews
    public void initializeAfterViews() {
        initToolbar();
        setFirebaseAuthListener();
    }

    @DebugLog
    @Override
    void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("SweetLoc");
    }

    @DebugLog
    public void setFirebaseAuthListener() {

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            updateUI(user);
        };

        mFirebaseAuth.addAuthStateListener(mAuthListener);

    }

    @DebugLog
    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity_.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        } else {
            SuperHelper.ReplaceFragmentBeginTransaction(
                    MainActivity.this,
                    NavFragment_.builder().build(),
                    R.id.Container,
                    false);
        }
    }

    @DebugLog
    @OnActivityResult(LOGIN_REQUEST_CODE)
    public void ActivityResultLogin(int resultCode) {
        switch (resultCode) {
            case RESULT_OK: {
                updateUI(FirebaseAuth.getInstance().getCurrentUser());
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @DebugLog
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuProfil: {
                //Intent activityIntent = new Intent(MainActivity.this, ProfileActivity_.class);
                ProfileActivity_.intent(this).start();
                //activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(activityIntent);
                break;
            }
            case R.id.menuMap: {
                Intent activityIntent = new Intent(this, MapsActivity_.class);
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(activityIntent);
                break;
            }
        }
        return true;
    }

    @DebugLog
    @Override
    protected void onStop() {
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
        super.onStop();
    }

}
