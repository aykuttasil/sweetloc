package com.aykuttasil.sweetloc.ui.fragment.roommemberlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.databinding.RoomMemberlistFragmentBinding
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import com.aykuttasil.sweetloc.ui.fragment.IFragmentContract
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import kotlinx.android.synthetic.main.room_memberlist_fragment.*
import javax.inject.Inject

class RoomMemberListFragment : BaseFragment(), IFragmentContract, Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: RoomMemberListViewModel
    lateinit var binding: RoomMemberlistFragmentBinding

    private val args: RoomMemberListFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.room_memberlist_fragment, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun initUiComponents() {
        listRoomMember.adapter = RoomMembersAdapter()
    }


    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RoomMemberListViewModel::class.java)
        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)
        viewModel.setRoomMemberList(args.roomId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun getViewModel(): BaseAndroidViewModel {
        return viewModel
    }

}
