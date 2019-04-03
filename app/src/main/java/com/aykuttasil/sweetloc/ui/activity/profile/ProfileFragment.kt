package com.aykuttasil.sweetloc.ui.activity.profile

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.aykuttasil.sweetloc.databinding.FragmentProfileBinding
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.di.ViewModelFactory
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import com.aykuttasil.sweetloc.util.BaseAndroidViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

open class ProfileFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: ProfileViewModel
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(
            view: View,
            savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setInformation()
        TextViewEmail.movementMethod = ScrollingMovementMethod()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
        binding.lifecycleOwner = this
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

    override fun getViewModel(): BaseAndroidViewModel {
        return viewModel
    }
}
