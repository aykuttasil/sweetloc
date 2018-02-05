package com.aykuttasil.sweetloc.ui.fragment.usertrackerlist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import com.aykuttasil.sweetloc.util.adapter.UserTrackerListAdapter
import hugo.weaving.DebugLog
import kotlinx.android.synthetic.main.fragment_user_tracker_list_layout.*
import javax.inject.Inject

open class UserTrackerListFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var mAdapter: UserTrackerListAdapter

    private lateinit var userTrackerListViewModel: UserTrackerListViewModel

    @DebugLog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_tracker_list_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerViewAdapter()
        userTrackerListViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserTrackerListViewModel::class.java)
        userTrackerListViewModel.getTrackerList().observe(this, android.arch.lifecycle.Observer {
            it?.let {
                mAdapter.clearAndAddItemList(it)
            }
        })
    }

    private fun initRecyclerViewAdapter() {
        RecyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        RecyclerView!!.adapter = mAdapter
    }

}
