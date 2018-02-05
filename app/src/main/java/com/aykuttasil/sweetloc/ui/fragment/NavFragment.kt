package com.aykuttasil.sweetloc.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.aykuttasil.sweetloc.R

/**
 * Created by aykutasil on 5.12.2016.
 */
open class NavFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nav_layout, container, false)
    }

}
