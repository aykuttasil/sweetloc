package com.aykuttasil.sweetloc.ui

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.aykuttasil.sweetloc.App
import com.aykuttasil.sweetloc.model.process.DataOkCancelDialog
import com.aykuttasil.sweetloc.util.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseAndroidViewModel(app: App) : AndroidViewModel(app), CoroutineScope, LifecycleObserver {

    var jobs = Job()
    var disposables = CompositeDisposable()

    val liveSnackbar = SingleLiveEvent<String>()
    val liveProgress = MutableLiveData<Boolean>()
    val liveConfirmDialog = MutableLiveData<DataOkCancelDialog>()

    override val coroutineContext: CoroutineContext
        get() = jobs + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        jobs.cancel()
    }
}
