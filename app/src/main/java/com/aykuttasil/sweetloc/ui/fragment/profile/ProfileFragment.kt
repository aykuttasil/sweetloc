package com.aykuttasil.sweetloc.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.databinding.FragmentProfileLayoutBinding
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import com.aykuttasil.sweetloc.util.BaseAndroidViewModel
import javax.inject.Inject

class ProfileFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: ProfileViewModel
    lateinit var binding: FragmentProfileLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_layout, container, false)
        return binding.root
    }

    override fun initUiComponents() {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        initGlobalObservers()
    }

    override fun getViewModel(): BaseAndroidViewModel {
        return viewModel
    }

    private fun setInformation() {
        /*
        async(UI)
        {
            val user = bg { dataManager.getUserEntity() }.await()
            binding.user = user

            TextViewEmail.text = user?.userEmail
            Picasso.with(this@ProfileFragment.context)
                .load(user?.userImageUrl)
                .transform(PicassoCircleTransform())
                .into(ImageViewProfilePicture)
        }
        */
    }

}
