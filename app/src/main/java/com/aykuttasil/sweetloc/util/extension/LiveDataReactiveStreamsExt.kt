package com.aykuttasil.sweetloc.util.extension

/**
 * Created by aykutasil on 27.12.2017.
 */
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import org.reactivestreams.Publisher

fun <T> Publisher<T>.toLiveData() = LiveDataReactiveStreams.fromPublisher(this)

fun <T> LiveData<T>.toPublisher(lifecycleOwner: LifecycleOwner)
        = LiveDataReactiveStreams.toPublisher(lifecycleOwner, this)