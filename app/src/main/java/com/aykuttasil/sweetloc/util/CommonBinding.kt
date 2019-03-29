package com.aykuttasil.sweetloc.util

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

interface BindableAdapter<T> {
    fun setData(items: List<T>)
}

@BindingAdapter("listData")
fun <T> recyclerViewDataListBinding(recyclerView: RecyclerView, list: List<T>?) {
    if (recyclerView.adapter is BindableAdapter<*>) {
        list?.apply {
            (recyclerView.adapter as BindableAdapter<T>).setData(this)
        }
    }
}

@BindingAdapter("visibleGone")
fun viewVisibleBinding(view: View, isVisible: Boolean) {
    if (isVisible) view.visibility = View.VISIBLE
    else view.visibility = View.GONE
}