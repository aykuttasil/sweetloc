package com.aykuttasil.sweetloc.fragment;

import android.view.WindowManager;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.activity.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Created by aykutasil on 5.12.2016.
 */
@EFragment(R.layout.fragment_nav_layout)
public class NavFragment extends BaseFragment {

    @AfterViews
    @Override
    void initializeAfterViews() {

    }
}
