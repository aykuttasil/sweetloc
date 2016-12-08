package com.aykuttasil.sweetloc.fragment;

import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.activity.ProfileActivity;
import com.aykuttasil.sweetloc.helper.SuperHelper;
import com.facebook.login.LoginManager;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.onesignal.OneSignal;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_profile)
public class ProfileActivityFragment extends BaseFragment {

    @ViewById(R.id.TextView_PeridicTime)
    TextView mTextView_PeriodicTime;
    //
    ProfileActivity mActivity;

    @DebugLog
    @AfterViews
    public void initializeAfterViews() {
        mActivity = (ProfileActivity) getActivity();
        setInformation();
        mTextView_PeriodicTime.setMovementMethod(new ScrollingMovementMethod());
    }

    @DebugLog
    private void setInformation() {

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                OneSignal.idsAvailable((userId, registrationId) -> {
                    Logger.i("OneSignal userId: " + userId);
                    Logger.i("OneSignal regId: " + registrationId);

                    subscriber.onNext(registrationId);
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {

                    long periodicTime = FirebaseRemoteConfig.getInstance().getLong("periodic_time");

                    String text = "";
                    text += "Periodic Time: " + periodicTime + "\n";
                    text += "Reg id: " + result;

                    mTextView_PeriodicTime.setText(text);
                });
    }

    @DebugLog
    @Click(R.id.Button_CikisYap)
    public void Button_CikisYapClick() {
        LoginManager.getInstance().logOut();
        SuperHelper.ResetSweetLoc();
        mActivity.goLoginActivity();
        //mActivity.stopPeriodicTask(getContext());
        //PeriodicService_.intent(getContext()).stop();
        //mActivity.finish();
    }
}
