/* Author - Aykut Asil(aykuttasil) */
package com.aykuttasil.sweetloc.ui.activity.login

import android.view.View
import androidx.databinding.BindingAdapter
import com.aykuttasil.sweetloc.data.Resource

@BindingAdapter("showWhen")
fun <T, E> showWhenBinding(vi: View, resource: Resource<T, E>?) {
    if (resource?.status == Resource.Status.LOADING) {
        vi.visibility = View.VISIBLE
    } else {
        vi.visibility = View.GONE
    }
}
