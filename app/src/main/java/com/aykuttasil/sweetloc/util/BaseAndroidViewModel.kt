package com.aykuttasil.sweetloc.util

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import com.aykuttasil.sweetloc.App
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseAndroidViewModel(app: App) : AndroidViewModel(app), CoroutineScope, LifecycleObserver {

    var jobs = Job()
    var disposables = CompositeDisposable()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + jobs


    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        jobs.cancel()
    }
}
