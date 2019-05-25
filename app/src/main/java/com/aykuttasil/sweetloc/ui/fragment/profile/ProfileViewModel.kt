package com.aykuttasil.sweetloc.ui.fragment.profile

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.helper.SweetLocHelper
import com.aykuttasil.sweetloc.model.process.DataOkCancelDialog
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import com.aykuttasil.sweetloc.util.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    val app: Application,
    val userRepository: UserRepository,
    val sweetLocHelper: SweetLocHelper
) : BaseAndroidViewModel(app) {

    val liveUser: MutableLiveData<UserEntity> = MutableLiveData()
    val liveLogout: MutableLiveData<Boolean> = SingleLiveEvent()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onSetup() {
        launch {
            liveUser.value = userRepository.getUserEntity()
        }
    }

    fun logoutClick() {
        liveConfirmDialog.value = DataOkCancelDialog(
            "Çıkış Yap",
            "Devam edilsin mi?",
            actionOkText = "Ok",
            actionCancelText = "Cancel",
            actionOk = {
                sweetLocHelper.resetSweetLoc(app)
                liveLogout.value = true
            },
            actionCancel = {

            })

        /*
        val dialog = UiHelper.UiDialog.newInstance(app)
                .getOKCancelDialog("Çıkış Yap", "Devam edilsin mi?", null)

        dialog.positiveButton {
            dialog.dismiss()
            sweetLocHelper.resetSweetLoc(app)
            val intent = Intent(app, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            app.startActivity(intent)
            // app.finish()
        }
        dialog.show()
         */
    }
}
