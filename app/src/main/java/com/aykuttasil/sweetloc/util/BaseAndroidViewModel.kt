package com.aykuttasil.sweetloc.util

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.aykuttasil.sweetloc.App
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseAndroidViewModel(app: App) : AndroidViewModel(app), CoroutineScope {

    var jobs = Job()
    var disposables = CompositeDisposable()

    val liveSnackbar = MutableLiveData<String>()
    val liveProgress = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = jobs + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        jobs.cancel()
    }
}
