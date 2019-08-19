/* Author - Aykut Asil(@aykuttasil) */
package com.aykuttasil.sweetloc.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aykuttasil.androidbasichelperlib.UiHelper
import com.aykuttasil.sweetloc.ui.activity.base.BaseActivity
import com.aykuttasil.sweetloc.util.extension.TOAST
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), IFragmentContract, CoroutineScope {

    abstract fun initUiComponents()
    abstract fun initViewModel()

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private fun initGlobalObservers() {
        getViewModel().liveSnackbar.observe(viewLifecycleOwner, Observer {
            TOAST(it)
        })

        getViewModel().liveConfirmDialog.observe(viewLifecycleOwner, Observer {
            val dialog = UiHelper.UiDialog.newInstance(context!!).getOKCancelDialog(it.title, it.content, it.icon)
            dialog.positiveButton(text = it.actionOkText) { d ->
                d.dismiss()
                it.actionOk()
            }

            dialog.negativeButton(text = it.actionCancelText) { d ->
                d.dismiss()
                it.actionCancel()
            }

            dialog.show()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUiComponents()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        initGlobalObservers()
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

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }
}
