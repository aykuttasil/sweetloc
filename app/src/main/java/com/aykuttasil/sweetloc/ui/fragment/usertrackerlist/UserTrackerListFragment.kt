package com.aykuttasil.sweetloc.ui.fragment.usertrackerlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import com.aykuttasil.sweetloc.util.adapter.UserTrackerListAdapter
import kotlinx.android.synthetic.main.fragment_user_tracker_list_layout.*
import javax.inject.Inject

open class UserTrackerListFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var mAdapter: UserTrackerListAdapter

    private lateinit var userTrackerListViewModel: UserTrackerListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_tracker_list_layout, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userTrackerListViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(UserTrackerListViewModel::class.java)

        userTrackerListViewModel.getTrackerList().observe(this, androidx.lifecycle.Observer {
            it?.let {
                mAdapter.clearAndAddItemList(it)
            }
        })
    }

    private fun setUI() {
        listUserTracker?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        listUserTracker?.adapter = mAdapter
    }
}
