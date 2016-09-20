package com.aykuttasil.sweetloc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.fragment.MainFragment;
import com.aykuttasil.sweetloc.fragment.MainFragment_;
import com.aykuttasil.sweetloc.fragment.MapFragment;
import com.aykuttasil.sweetloc.fragment.MapFragment_;
import com.aykuttasil.sweetloc.fragment.ProfileFragment;
import com.aykuttasil.sweetloc.fragment.ProfileFragment_;
import com.aykuttasil.sweetloc.helper.SuperHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 23.06.2016.
 */
@EActivity(R.layout.activity_main_layout)
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawer;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;
    //
    ActionBarDrawerToggle toggle;
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener mAuthListener;

    @DebugLog
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @DebugLog
    @AfterViews
    public void MainActivityInit() {
        setNavigationView();
        startFirebaseInstanceIDService();
        setFirebaseAuthListener();
    }

    @DebugLog
    public void setNavigationView() {
        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @DebugLog
    public void setFirebaseAuthListener() {

        if (!SuperHelper.checkUser()) {

            mAuthListener = firebaseAuth -> {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Logger.d("onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Logger.d("onAuthStateChanged:signed_out");
                }
                updateUI(user);
            };
            mFirebaseAuth.addAuthStateListener(mAuthListener);
        }

    }

    @DebugLog
    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity_.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        } else {
            startFragment();
        }
    }

    @DebugLog
    private void startFragment() {
        SuperHelper.ReplaceFragmentBeginTransaction(
                this,
                MainFragment_.builder().build(),
                FRAGMENT_CONTAINER_ID,
                false);
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

    @DebugLog
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuProfil: {
                //WhichFragment = MainGonderiFragment.class.getSimpleName();
                //WhichNestedFragment = GonderiListFragment.class.getSimpleName();
                SuperHelper.ReplaceFragmentBeginTransaction(
                        this,
                        ProfileFragment_.builder().build(),
                        FRAGMENT_CONTAINER_ID,
                        ProfileFragment.class.getSimpleName(),
                        false);
                break;
            }
            case R.id.menuHarita: {
                //WhichFragment = MainGonderiFragment.class.getSimpleName();
                //WhichNestedFragment = GonderiListFragment.class.getSimpleName();
                SuperHelper.ReplaceFragmentBeginTransaction(
                        this,
                        MapFragment_.builder().build(),
                        FRAGMENT_CONTAINER_ID,
                        MapFragment.class.getSimpleName(),
                        false);
                break;
            }
            default: {
                //WhichFragment = MainGonderiFragment.class.getSimpleName();
                SuperHelper.ReplaceFragmentBeginTransaction(
                        this,
                        MainFragment_.builder().build(),
                        FRAGMENT_CONTAINER_ID,
                        MainFragment.class.getSimpleName(),
                        false);
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @DebugLog
    @Override
    protected void onStop() {
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
        super.onStop();
    }

    @DebugLog
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
