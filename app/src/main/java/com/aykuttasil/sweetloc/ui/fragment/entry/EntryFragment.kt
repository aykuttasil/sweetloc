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
import com.aykuttasil.sweetloc.ui.activity.login.LoginActivity
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import com.google.firebase.database.DatabaseReference
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

        btnAction.setOnClickListener {
            val user = userRepository.getUserEntity()
            userRepository.processUserToRemote(user!!.userId) {
                user.userEmail = "testetstetstst123@gmail.com"
                it.updateChildren(mapOf("userEmail" to user.userEmail))
            }.subscribe()
        }

        btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EntryViewModel::class.java)
    }

    override fun initUiComponents() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun getViewModel(): BaseAndroidViewModel {
        return viewModel
    }


}
