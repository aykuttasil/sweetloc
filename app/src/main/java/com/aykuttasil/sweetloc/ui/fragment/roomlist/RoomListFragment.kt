package com.aykuttasil.sweetloc.ui.fragment.roomlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.databinding.FragmentRoomListLayoutBinding
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_room_list_layout.*
import javax.inject.Inject

open class RoomListFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var listAdapter: RoomsAdapter

    private lateinit var viewModel: RoomListViewModel
    lateinit var binding: FragmentRoomListLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomListLayoutBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun initUiComponents() {
        listRoom?.adapter = listAdapter
        fab.setOnClickListener {
            it.findNavController().navigate(R.id.action_roomListFragment_to_roomCreateFragment)
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RoomListViewModel::class.java)
        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)
    }

    override fun getViewModel(): BaseAndroidViewModel {
        return viewModel
    }
}
