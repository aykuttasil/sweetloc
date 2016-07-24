package com.aykuttasil.sweetloc.fragment;

import android.content.Context;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.activity.MainActivity;
import com.aykuttasil.sweetloc.helper.SuperHelper;
import com.aykuttasil.sweetloc.service.PeriodicService_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 10.07.2016.
 */
@EFragment(R.layout.fragment_profil_layout)
public class ProfilFragment extends BaseFragment {

    Context mContext;
    MainActivity mActivity;

    @DebugLog
    @AfterViews
    public void ProfilFragmentInit() {
        this.mContext = getContext();
        this.mActivity = (MainActivity) getActivity();
        //
    }

    @DebugLog
    @Click(R.id.Button_CikisYap)
    public void Button_CikisYapClick() {
        SuperHelper.ResetSweetLoc();
        stopPeriodicTask(mContext);
        PeriodicService_.intent(mContext).stop();
    }
}
