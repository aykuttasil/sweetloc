package com.aykuttasil.sweetloc.ui.activity.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.aykuttasil.sweetloc.App
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.model.process.DataOkDialog
import com.aykuttasil.sweetloc.util.BaseAndroidViewModel
import com.aykuttasil.sweetloc.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

inline fun <T> dependantLiveData(
        vararg dependencies: LiveData<*>,
        crossinline mapper: () -> T?
) = MediatorLiveData<T>().also { mediatorLiveData ->
    val observer = Observer<Any> { mediatorLiveData.value = mapper() }
    dependencies.forEach { dependencyLiveData ->
        mediatorLiveData.addSource(dependencyLiveData, observer)
    }
}

sealed class LoginUiStates
data class LoginUiStateSuccessfulLogin(val user: UserEntity) : LoginUiStates()
data class LoginUiStateSuccessfulRegister(val dataOkDialog: DataOkDialog) : LoginUiStates()
data class LoginUiStateError(val dataOkDialog: DataOkDialog) : LoginUiStates()
data class LoginUiStateProgress(val progressMsg: String? = "Lütfen Bekleyiniz.") : LoginUiStates()

open class LoginViewModel @Inject constructor(
        private val app: App,
        private val userRepository: UserRepository
) : BaseAndroidViewModel(app) {

    val liveUiStates = MutableLiveData<LoginUiStates>()
    val liveSnackbar = SingleLiveEvent<String>()
    val liveOkDialog = SingleLiveEvent<DataOkDialog>()
    val liveEmail = MutableLiveData<String>()
    val livePass = MutableLiveData<String>()
    val displayName = dependantLiveData(liveEmail) {
        liveEmail.value ?: "null"
    }

    open fun login(
            email: String,
            password: String
    ) {
        liveUiStates.value = LoginUiStateProgress()

        userRepository.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    liveSnackbar.value = "${it?.userEmail} ile oturum açıldı."
                    liveUiStates.value = LoginUiStateSuccessfulLogin(it)
                }, {
                    it.printStackTrace()
                    val dialog = DataOkDialog("SweetLoc", it?.message ?: "") {}
                    liveUiStates.value = LoginUiStateError(dialog)
                }).addTo(disposables)

    }

    fun register(
            email: String,
            password: String
    ) {
        userRepository.registerUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    liveSnackbar.value = "${it?.userEmail} ile kayıt olundu."
                    liveUiStates.value = LoginUiStateSuccessfulLogin(it)
                }, {
                    it.printStackTrace()
                    val dialog = DataOkDialog("SweetLoc", it?.message ?: "") {}
                    liveUiStates.value = LoginUiStateError(dialog)
                }).addTo(disposables)
    }
}