package com.aykuttasil.sweetloc.util

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