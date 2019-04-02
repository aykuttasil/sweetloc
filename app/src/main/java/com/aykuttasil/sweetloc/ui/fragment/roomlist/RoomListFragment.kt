package com.aykuttasil.sweetloc.ui.fragment.roomlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.databinding.FragmentRoomListLayoutBinding
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_room_list_layout.*
import javax.inject.Inject

open class RoomListFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var listAdapter: RoomsAdapter

    private lateinit var roomListViewModel: RoomListViewModel
    lateinit var binding: FragmentRoomListLayoutBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_list_layout, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(
            view: View,
            savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    private fun setUI() {
        listUserTracker?.adapter = listAdapter
        fab.setOnClickListener {
            findNavController(this).navigate(R.id.action_roomListFragment_to_roomCreateFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        roomListViewModel = ViewModelProviders.of(this, viewModelFactory).get(RoomListViewModel::class.java)
        lifecycle.addObserver(roomListViewModel)
        binding.viewModel = roomListViewModel
    }
}
