package com.aykuttasil.sweetloc.ui.activity.login

import android.view.View
import androidx.databinding.BindingAdapter
import com.aykuttasil.sweetloc.data.remote.Resource


@BindingAdapter("showWhen")
fun <T, E> showWhenAdapter(vi: View, resource: Resource<T, E>?) {
    if (resource?.status == Resource.Status.LOADING) {
        vi.visibility = View.VISIBLE
    } else {
        vi.visibility = View.GONE
    }
}
