package com.aykuttasil.sweetloc.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.aykuttasil.androidbasichelperlib.UiHelper;
import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.helper.SuperHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;

@EActivity(R.layout.activity_profile)
public class ProfileActivity extends BaseActivity {

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
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @DebugLog
    @Override
    void updateUi() {

    }

    @DebugLog
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionCikisYap: {

                MaterialDialog dialog = UiHelper.UiDialog.newInstance(this).getOKCancelDialog("Çıkış Yap", "Devam edilsin mi?", null);
                dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(view -> {

                    dialog.dismiss();

                    SuperHelper.ResetSweetLoc(this);

                    goLoginFacebookActivity(ProfileActivity.this);
                });

                dialog.show();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @DebugLog
    @Override
    public void onStop() {
        super.onStop();
    }
}
