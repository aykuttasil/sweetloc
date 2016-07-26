package com.aykuttasil.sweetloc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.service.MyFirebaseInstanceIdService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.logger.Logger;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 23.06.2016.
 */
public class BaseActivity extends AppCompatActivity {

    public static final int FRAGMENT_CONTAINER_ID = R.id.fragment_container;
    public static final int LOGIN_REQUEST_CODE = 1001;
    //


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @DebugLog
    public void startFirebaseInstanceIDService() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Logger.i(token);
        if (token == null) {
            Intent intent = new Intent(this, MyFirebaseInstanceIdService.class);
            startService(intent);
        }
    }

    @DebugLog
    public void SetNavigationHeader(MainActivity activity) {
        NavigationView navHeader = (NavigationView) activity.findViewById(R.id.nav_view);
        TextView email = (TextView) navHeader.getHeaderView(0).findViewById(R.id.navigationEmail);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email.setText(user.getEmail());
    }

}
