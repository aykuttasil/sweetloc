/* Author - Aykut Asil(aykuttasil) */
package com.aykuttasil.sweetloc.util

import androidx.lifecycle.ViewModel
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