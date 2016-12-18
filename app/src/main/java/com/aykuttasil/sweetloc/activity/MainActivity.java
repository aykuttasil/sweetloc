package com.aykuttasil.sweetloc.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.db.DbManager;
import com.aykuttasil.sweetloc.fragment.UserTrackerListFragment_;
import com.aykuttasil.sweetloc.helper.SuperHelper;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.onesignal.OneSignal;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

    @DebugLog
    @AfterViews
    public void initializeAfterViews() {
        initToolbar();
        updateUi();
    }

    @DebugLog
    @Override
    void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("SweetLoc");
    }

    @DebugLog
    @Override
    void updateUi() {

        if (!SuperHelper.checkUser()) {
            SuperHelper.logoutUser();
            goLoginFacebookActivity(this);
        } else {
            initMain();
        }
    }

    @DebugLog
    private void initMain() {

        SuperHelper.startPeriodicTask(this);

        if (DbManager.getOneSignalUserId() == null) {

            Observable.create(
                    new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(Subscriber<? super String> subscriber) {

                            OneSignal.idsAvailable((userId, registrationId) -> {

                                Logger.i("OneSignal userId: " + userId);
                                Logger.i("OneSignal regId: " + registrationId);

                                subscriber.onNext(userId);

                            });
                        }
                    })
                    .flatMap(new Func1<String, Observable<String>>() {
                        @Override
                        public Observable<String> call(String userId) {

                            ModelUser modelUser = DbManager.getModelUser();
                            modelUser.setOneSignalUserId(userId);
                            modelUser.save();

                            SuperHelper.updateUser(modelUser);

                            return Observable.just(userId);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        goFragment();
                    }, SuperHelper::CrashlyticsError);
        } else {
            goFragment();
        }

    }

    @DebugLog
    private void goFragment() {

        SuperHelper.ReplaceFragmentBeginTransaction(
                MainActivity.this,
                UserTrackerListFragment_.builder().build(),
                R.id.Container,
                false);
    }

    /**
     * Activity singleTop modunda çalıştırılır ise ve
     * bu Activity zaten görünür durumda ise
     * yani stack in en üstünde ise startActivity() ile bu activity ye gönderilen intent buraya ulaşır.
     * Activity nin yeni bir instance ı oluşturulmaz.
     *
     * @param intent
     */
    @DebugLog
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        updateUi();
    }

    @DebugLog
    @OnActivityResult(LOGIN_REQUEST_CODE)
    public void ActivityResultLogin(int resultCode) {

        switch (resultCode) {
            case RESULT_OK: {
                updateUi();
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
                ProfileActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK).start();
                break;
            }
            case R.id.menuMap: {
                MapsActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK).start();
                break;
            }
        }
        return true;
    }


    @DebugLog
    @Override
    protected void onStop() {
        super.onStop();
    }

    @DebugLog
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
