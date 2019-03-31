package com.aykuttasil.sweetloc.ui.fragment

import androidx.fragment.app.Fragment
import com.aykuttasil.sweetloc.ui.activity.base.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

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
