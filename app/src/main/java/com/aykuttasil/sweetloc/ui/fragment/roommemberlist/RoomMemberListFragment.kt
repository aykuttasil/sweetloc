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
import com.aykuttasil.sweetloc.ui.activity.map.MapsActivityArgs
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import kotlinx.android.synthetic.main.room_memberlist_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.support.v4.share
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RoomMemberListFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val viewModel by viewModels<RoomMemberListViewModel> { viewModelFactory }

    lateinit var binding: RoomMemberlistFragmentBinding

    private val args: RoomMemberListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.room_memberlist_fragment, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.isDeepLinkforMemberRecord) {
            viewModel.addMember(args.roomId)
        }
    }

    override fun initUiComponents() {
        listRoomMember.adapter = RoomMembersAdapter()

        fabBottomNavigation.setOnClickListener {
            launch {
                // val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                // bottomNavDrawerFragment.show(childFragmentManager, bottomNavDrawerFragment.tag)

                val link = generateRoomLink(args.roomId, args.roomName!!)
                share("Sweetloc Room Link For ${args.roomName} : ${link.shortLink}", "Add Member")
            }
        }

        bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.itemMap -> {
                    findNavController().navigate(R.id.mapsActivity, MapsActivityArgs(args.roomId).toBundle())
                    true
                }
                else -> {
                    false
                }
            }
        }
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
                launch {
                    val link = generateRoomLink(args.roomId, args.roomName!!)
                    println(link)
                }
            }
        }
        return true
    }

    private suspend fun generateRoomLink(roomId: String, roomName: String): ShortDynamicLink {
        return withContext(Dispatchers.Default) {
            suspendCoroutine<ShortDynamicLink> { cont ->
                val baseUrl = Uri.parse("https://aykuttasil.github.io/sweetloc/rooms/$roomId?roomName=$roomName")

                FirebaseDynamicLinks.getInstance()
                    .createDynamicLink()
                    .setLink(baseUrl)
                    .setDomainUriPrefix(getString(R.string.dynamic_links_uri_prefix))
                    // .setNavigationInfoParameters()
                    .setAndroidParameters(DynamicLink.AndroidParameters.Builder(BuildConfig.APPLICATION_ID).build())
                    .buildShortDynamicLink()
                    .addOnSuccessListener {
                        cont.resume(it)
                    }
            }
        }
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
