/* Author - Aykut Asil(aykuttasil) */
package com.aykuttasil.sweetloc.util.extension

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(
    snackbarText: String?,
    timeLength: Int = Snackbar.LENGTH_LONG
) {
    Snackbar.make(this, snackbarText ?: "Message is null", timeLength).show()
}

/**
 * Fragent extension for [Snackbar]
 */
fun Fragment.showSnackbar(snackbarText: String, timeLength: Int) {
    activity?.let { Snackbar.make(it.findViewById<View>(android.R.id.content), snackbarText, timeLength).show() }
}

/*
/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(lifecycleOwner: LifecycleOwner,
                       snackbarMessageLiveEvent: SingleLiveEvent<Int>, timeLength: Int) {
    snackbarMessageLiveEvent.observe(lifecycleOwner, Observer {
        it?.let { showSnackbar(context.getString(it), timeLength) }
    })
}
*/

/**
 * Reloads the data when the pull-to-refresh is triggered.
 *
 * Creates the `android:onRefresh` for a [SwipeRefreshLayout].
 */
// @BindingAdapter("android:onRefresh")
// fun ScrollChildSwipeRefreshLayout.setSwipeRefreshLayoutOnRefreshListener(
//        viewModel: TasksViewModel) {
//    setOnRefreshListener { viewModel.loadTasks(true) }
// }