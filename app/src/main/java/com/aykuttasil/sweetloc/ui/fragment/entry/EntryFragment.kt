package com.aykuttasil.sweetloc.ui.fragment.entry

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.helper.SweetLocHelper
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import com.aykuttasil.sweetloc.ui.activity.login.LoginActivity
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DatabaseReference
import com.skydoves.needs.*
import kotlinx.android.synthetic.main.fragment_entry.*
import javax.inject.Inject

class EntryFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var databaseReference: DatabaseReference

    @Inject
    lateinit var sweetLocHelper: SweetLocHelper

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: EntryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isLogin = userRepository.checkUser()
        if (!isLogin) {
            startActivity(Intent(activity, LoginActivity::class.java))
        }


        btnGoUserTrackerList.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_roomListFragment)
        }

        btnCleanAllData.setOnClickListener {
            showProgress()
            databaseReference.removeValue().addOnCompleteListener {
                dismissProgress()
            }
        }

        btnLogout.setOnClickListener {
            sweetLocHelper.resetSweetLoc(context!!)
        }

        val needs = initNeeds()
        btnAction.setOnClickListener {
            needs.show(view)
            /*
            val user = userRepository.getUserEntity()
            userRepository.processUserToRemote(user!!.userId) {
                user.userEmail = "testetstetstst123@gmail.com"
                it.updateChildren(mapOf("userEmail" to user.userEmail))
            }.subscribe()
            */
        }

        btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
        }
    }

    private fun initNeeds(): Needs {
        val needs = createNeeds(context!!) {
            // titleIcon = null
            title = "Permission instructions \nfor using this Android app."
            // titleTextForm = null
            addNeedsItem(NeedsItem(null, "SD Card", "(Required)", "Access photos, media, and files on device."))
            addNeedsItem(NeedsItem(null, "Location", "(Required)", "Access this device's location."))
            addNeedsItem(NeedsItem(null, "Camera", "(Optional)", "Take pictures and record video."))
            addNeedsItem(NeedsItem(null, "Contact", "(Optional)", "Access this device's contacts."))
            addNeedsItem(NeedsItem(null, "SMS", "(Optional)", "Send and view SMS messages."))
            description = "The above accesses are used to better serve you."
            confirm = "Confirm"
            backgroundAlpha = 0.6f
            lifecycleOwner = this@EntryFragment
            //needsTheme = theme
            //needsItemTheme = itemTheme
            needsAnimation = NeedsAnimation.CIRCULAR
        }

        needs.setOnConfirmListener(object : OnConfirmListener {
            override fun onConfirm() {
                needs.dismiss()
                viewModel.liveSnackbar.value = "Confirmed"
            }

        })
        return needs
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EntryViewModel::class.java)
    }

    override fun initUiComponents() {

    }

    override fun getViewModel(): BaseAndroidViewModel {
        return viewModel
    }


}
