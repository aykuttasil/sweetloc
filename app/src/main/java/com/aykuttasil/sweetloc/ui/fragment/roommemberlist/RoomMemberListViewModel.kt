package com.aykuttasil.sweetloc.ui.fragment.roommemberlist

import androidx.lifecycle.MutableLiveData
import com.aykuttasil.sweetloc.App
import com.aykuttasil.sweetloc.util.BaseAndroidViewModel
import javax.inject.Inject

class RoomMemberListViewModel @Inject constructor(
        private val app: App
) : BaseAndroidViewModel(app) {

    val liveSnackbar = MutableLiveData<String>()
    val liveProgress = MutableLiveData<Boolean>()
}