package com.aykuttasil.sweetloc.ui.fragment.entry


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.ui.activity.login.LoginActivity
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_entry.*
import javax.inject.Inject

class EntryFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var userRepository: UserRepository

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
            findNavController().navigate(R.id.action_mainFragment_to_userTrackerListFragment3)
        }
    }
}
