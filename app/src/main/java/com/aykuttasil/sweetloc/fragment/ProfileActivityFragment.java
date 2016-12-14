package com.aykuttasil.sweetloc.fragment;

import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.activity.ProfileActivity;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.onesignal.OneSignal;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
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

    }

}
