package com.aykuttasil.sweetloc.ui.fragment.profile

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.aykuttasil.sweetloc.App
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.helper.SweetLocHelper
import com.aykuttasil.sweetloc.model.process.DataOkCancelDialog
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import com.aykuttasil.sweetloc.ui.activity.main.MainActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        val app: App,
        val userRepository: UserRepository,
        val sweetLocHelper: SweetLocHelper
) : BaseAndroidViewModel(app) {

    val liveUser: MutableLiveData<UserEntity> = MutableLiveData()

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
                    val intent = Intent(app, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    app.startActivity(intent)
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
