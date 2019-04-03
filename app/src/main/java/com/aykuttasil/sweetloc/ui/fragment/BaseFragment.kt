package com.aykuttasil.sweetloc.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aykuttasil.sweetloc.ui.activity.base.BaseActivity
import com.aykuttasil.sweetloc.util.extension.TOAST
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), IFragmentContract, CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun initObservers() {
        getViewModel().liveSnackbar.observe(viewLifecycleOwner, Observer {
            TOAST(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUiComponents()
    }

    abstract fun initUiComponents()

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
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
