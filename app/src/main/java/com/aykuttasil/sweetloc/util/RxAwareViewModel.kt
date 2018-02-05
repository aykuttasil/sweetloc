package com.aykuttasil.sweetloc.util

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class RxAwareViewModel : ViewModel() {

    var disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }
}