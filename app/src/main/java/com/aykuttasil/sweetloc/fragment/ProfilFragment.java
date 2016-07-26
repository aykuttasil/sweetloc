package com.aykuttasil.sweetloc.fragment;

import android.content.Context;
import android.widget.TextView;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.activity.MainActivity;
import com.aykuttasil.sweetloc.helper.SuperHelper;
import com.aykuttasil.sweetloc.service.PeriodicService_;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 10.07.2016.
 */
@EFragment(R.layout.fragment_profil_layout)
public class ProfilFragment extends BaseFragment {

    @ViewById(R.id.TextView_PeridicTime)
    TextView mTextView_PeriodicTime;
    //
    Context mContext;
    MainActivity mActivity;

    @DebugLog
    @AfterViews
    public void ProfilFragmentInit() {
        this.mContext = getContext();
        this.mActivity = (MainActivity) getActivity();
        //
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
        stopPeriodicTask(mContext);
        PeriodicService_.intent(mContext).stop();
    }
}
