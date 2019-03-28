package com.aykuttasil.sweetloc.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aykuttasil.sweetloc.ui.activity.base.BaseActivity

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun getBaseActivity(): BaseActivity {
        return activity as BaseActivity
    }

    fun showProgress() {
        getBaseActivity().showProgressDialog()
    }

    fun dismissProgress() {
        getBaseActivity().dismissProgressDialog()
    }

}
