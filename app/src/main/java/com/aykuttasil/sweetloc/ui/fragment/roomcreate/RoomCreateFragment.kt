package com.aykuttasil.sweetloc.ui.fragment.roomcreate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.aykuttasil.sweetloc.databinding.RoomCreateFragmentBinding
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import javax.inject.Inject

class RoomCreateFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: RoomCreateViewModel
    lateinit var binding: RoomCreateFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = RoomCreateFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun initUiComponents() {
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RoomCreateViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    override fun getViewModel(): BaseAndroidViewModel {
        return viewModel
    }

}
