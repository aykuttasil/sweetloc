/* Author - Aykut Asil(aykuttasil) */
package com.aykuttasil.sweetloc.util.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import org.reactivestreams.Publisher

fun <T> Publisher<T>.toLiveData() = LiveDataReactiveStreams.fromPublisher(this)

fun <T> LiveData<T>.toPublisher(lifecycleOwner: LifecycleOwner) = LiveDataReactiveStreams.toPublisher(lifecycleOwner, this)