package com.aykuttasil.sweetloc.ui.fragment.roommemberlist

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aykuttasil.sweetloc.BuildConfig
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.databinding.RoomMemberlistFragmentBinding
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.BaseAndroidViewModel
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import com.aykuttasil.sweetloc.ui.fragment.IFragmentContract
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import kotlinx.android.synthetic.main.room_memberlist_fragment.*
import javax.inject.Inject

class RoomMemberListFragment : BaseFragment(), IFragmentContract, Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val viewModel by viewModels<RoomMemberListViewModel> { viewModelFactory }

    lateinit var binding: RoomMemberlistFragmentBinding

    private val args: RoomMemberListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.room_memberlist_fragment, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun initUiComponents() {
        listRoomMember.adapter = RoomMembersAdapter()
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)
        viewModel.setRoomMemberList(args.roomId)
    }

    override fun getViewModel(): BaseAndroidViewModel {
        return viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.room_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemShare -> {
                val link = generateRoomLink(args.roomId, args.roomName!!)
                println(link)
            }
        }
        return true
    }

    fun createDeepLink() {
        findNavController().createDeepLink().setDestination(R.id.action_mainFragment_to_profileFragment)
            .createPendingIntent()
    }

    private fun generateRoomLink(roomId: String, roomName: String): Uri {
        val baseUrl = Uri.parse("https://sweetloc/rooms/$roomId?roomName=$roomName")
        val domain = "https://a3e34.app.goo.gl"

        val link = FirebaseDynamicLinks.getInstance()
            .createDynamicLink()
            .setLink(baseUrl)
            .setDomainUriPrefix(domain)
            // .setNavigationInfoParameters()
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder(BuildConfig.APPLICATION_ID).build())
            .buildDynamicLink()

        return link.uri
    }

    /*
    private fun onShareClicked() {
        val link = generateRoomLink(args.roomId)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, link.toString())

        startActivity(Intent.createChooser(intent, "Share Link"))
    }
    */
}
