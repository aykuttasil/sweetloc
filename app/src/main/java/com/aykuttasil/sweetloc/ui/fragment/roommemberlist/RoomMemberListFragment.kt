package com.aykuttasil.sweetloc.ui.fragment.roommemberlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.aykuttasil.sweetloc.databinding.RoomMemberlistFragmentBinding
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import com.aykuttasil.sweetloc.ui.fragment.IFragmentContract
import com.aykuttasil.sweetloc.util.BaseAndroidViewModel
import kotlinx.android.synthetic.main.room_memberlist_fragment.*
import javax.inject.Inject

class RoomMemberListFragment : BaseFragment(), IFragmentContract, Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: RoomMemberListViewModel
    lateinit var binding: RoomMemberlistFragmentBinding

    private val args: RoomMemberListFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = RoomMemberlistFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun initUiComponents() {
        listRoomMember.adapter = RoomMembersAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RoomMemberListViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        initObservers()
    }

    override fun getViewModel(): BaseAndroidViewModel {
        return viewModel
    }

}
