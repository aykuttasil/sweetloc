package com.aykuttasil.sweetloc.ui.activity.login

import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.aykuttasil.sweetloc.data.DataManager
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.model.process.DataOkDialog
import com.aykuttasil.sweetloc.util.RxAwareViewModel
import com.aykuttasil.sweetloc.util.SingleLiveEvent
import com.google.firebase.auth.FirebaseUser
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
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
data class LoginUiStateSuccessfulLogin(val user: FirebaseUser) : LoginUiStates()
data class LoginUiStateSuccessfulRegister(val dataOkDialog: DataOkDialog) : LoginUiStates()
data class LoginUiStateError(val dataOkDialog: DataOkDialog) : LoginUiStates()

open class LoginViewModel @Inject constructor(
        private val userRepository: UserRepository
) : RxAwareViewModel() {

    val liveUiStates = MutableLiveData<LoginUiStates>()

    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    /*
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)
    }
    */

    /**
     * Notifies observers that all properties of this instance have changed.
     */
    fun notifyChange() {
        //callbacks.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies observers that a specific property has changed. The getter for the
     * property that changes should be marked with the @Bindable annotation to
     * generate a field in the BR class to be used as the fieldId parameter.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        //callbacks.notifyCallbacks(this, fieldId, null)
    }

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
        disposables.add(userRepository.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Logger.i("Kullanıcı girişi başarılı.")
                    liveSnackbar.value = "${it?.email} ile oturum açıldı."
                    liveUiStates.value = LoginUiStateSuccessfulLogin(it)
                }, {
                    it.printStackTrace()
                    val dialog = DataOkDialog("SweetLoc", it?.message ?: "") {}
                    liveUiStates.value = LoginUiStateError(dialog)
                })
        )
    }

    fun register(
            email: String,
            password: String
    ) {
        disposables.add(userRepository.registerUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Logger.i("Kullanıcı kaydı başarılı.")
                    liveSnackbar.value = "${it?.email} ile kayıt olundu."
                    liveUiStates.value = LoginUiStateSuccessfulLogin(it)
                }, {
                    it.printStackTrace()
                    val dialog = DataOkDialog("SweetLoc", it?.message ?: "") {}
                    liveUiStates.value = LoginUiStateError(dialog)
                })
        )
    }
}