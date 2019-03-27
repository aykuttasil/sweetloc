package com.aykuttasil.sweetloc.ui.fragment.roomcreate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import javax.inject.Inject

class RoomCreateFragment : BaseFragment(), Injectable {

    companion object {
        fun newInstance() = RoomCreateFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: RoomCreateViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.room_create_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RoomCreateViewModel::class.java)
    }

}
