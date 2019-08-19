/* Author - Aykut Asil(@aykuttasil) */
package com.aykuttasil.sweetloc.ui.fragment.roomlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
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

    val viewModel by viewModels<RoomListViewModel> { viewModelFactory }

    lateinit var binding: FragmentRoomListLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_list_layout, container, false)
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
        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)
    }

    override fun getViewModel(): BaseAndroidViewModel {
        return viewModel
    }
}
