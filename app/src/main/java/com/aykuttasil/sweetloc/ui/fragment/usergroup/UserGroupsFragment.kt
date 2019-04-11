package com.aykuttasil.sweetloc.ui.fragment.usergroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import javax.inject.Inject

class UserGroupsFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: UserGroupsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_groups, container, false)
    }

    override fun initUiComponents() {

    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserGroupsViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun getViewModel(): BaseAndroidViewModel {
        return viewModel
    }
}