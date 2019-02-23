package com.aykuttasil.sweetloc.ui.fragment.usergroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.databinding.FragmentUserGroupsBinding
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import com.aykuttasil.sweetloc.util.delegates.Inflate
import javax.inject.Inject

/**
 * Created by aykutasil on 17.02.2018.
 */
class UserGroupsFragment : BaseFragment(), Injectable {

    private val binding: FragmentUserGroupsBinding by Inflate(R.layout.fragment_user_groups)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userGroupsViewModel: UserGroupsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userGroupsViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(UserGroupsViewModel::class.java)
    }
}