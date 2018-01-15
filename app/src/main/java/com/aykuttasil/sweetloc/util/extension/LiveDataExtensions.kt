package com.aykuttasil.sweetloc.util.extension

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.Observer
import io.reactivex.Flowable

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}

fun <T> LiveData<T>.toFlowable(owner: LifecycleOwner): Flowable<T> {
    return Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(owner, this))
}