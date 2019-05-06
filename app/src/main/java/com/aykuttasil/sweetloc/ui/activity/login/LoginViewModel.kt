package com.aykuttasil.sweetloc.ui.activity.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.aykuttasil.sweetloc.App
import com.aykuttasil.sweetloc.data.Resource
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.model.process.DataOkDialog
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import com.aykuttasil.sweetloc.util.SingleLiveEvent
import io.reactivex.Single
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

open class LoginViewModel @Inject constructor(
    private val app: App,
    private val userRepository: UserRepository
) : BaseAndroidViewModel(app) {

    val liveOkDialog = SingleLiveEvent<DataOkDialog>()
    val liveEmail = MutableLiveData<String>()
    val livePass = MutableLiveData<String>()

    val liveLogin = MutableLiveData<Resource<UserEntity, DataOkDialog>>()

    val displayName = dependantLiveData(liveEmail) {
        liveEmail.value ?: "null"
    }

    open fun login(
        email: String,
        password: String
    ) {
        liveLogin.value = Resource.loading()

        userRepository.loginUser(email, password)
            .observeOn(Schedulers.io())
            .flatMap { user ->
                val mapUser = mapOf(
                    "userId" to user.userId,
                    "userEmail" to user.userEmail,
                    "userPassword" to user.userPassword,
                    "userLastLoginDate" to user.userLastLoginDate
                )
                userRepository.updateUserToRemote(user.userId, mapUser)
                    .blockingGet()
                userRepository.addUserToLocal(user)
                Single.just(user)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                liveSnackbar.value = "${it?.userEmail} ile oturum açıldı."
                liveLogin.value = Resource.success(it)
            }, {
                it.printStackTrace()
                val dialog = DataOkDialog("SweetLoc", it?.message ?: "") {}
                liveLogin.value = Resource.error(it, dialog)
            }).addTo(disposables)
    }

    fun register(
        email: String,
        password: String
    ) {
        liveLogin.value = Resource.loading()

        userRepository.registerUser(email, password)
            .flatMap { userEntity ->
                userRepository.insertUserToRemote(userEntity)
            }
            .observeOn(Schedulers.io())
            .flatMap {
                userRepository.addUserToLocal(it)
                Single.just(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                liveSnackbar.value = "${it?.userEmail} ile oturum açıldı."
                liveLogin.value = Resource.success(it)
            }, {
                it.printStackTrace()
                val dialog = DataOkDialog("SweetLoc", it?.message ?: "") {}
                liveLogin.value = Resource.error(it, dialog)
            }).addTo(disposables)
    }
}