package com.aykuttasil.sweetloc.fragment;

import android.widget.TextView;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.activity.ProfileActivity;
import com.aykuttasil.sweetloc.helper.SuperHelper;
import com.aykuttasil.sweetloc.service.PeriodicService_;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;

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
    }

    @DebugLog
    private void setInformation() {
        long periodicTime = FirebaseRemoteConfig.getInstance().getLong("periodic_time");
        mTextView_PeriodicTime.setText("Periodic Time : " + periodicTime);
    }

    @DebugLog
    @Click(R.id.Button_CikisYap)
    public void Button_CikisYapClick() {
        SuperHelper.ResetSweetLoc();
        mActivity.stopPeriodicTask(getContext());
        PeriodicService_.intent(getContext()).stop();
        mActivity.finish();
    }
}
