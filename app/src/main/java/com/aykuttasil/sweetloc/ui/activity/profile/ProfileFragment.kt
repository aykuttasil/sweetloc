package com.aykuttasil.sweetloc.ui.activity.profile

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.data.DataManager
import com.aykuttasil.sweetloc.databinding.FragmentProfileBinding
import com.aykuttasil.sweetloc.di.Injectable
import com.aykuttasil.sweetloc.ui.fragment.BaseFragment
import com.aykuttasil.sweetloc.util.delegates.Inflate
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

open class ProfileFragment : BaseFragment(), Injectable {

    @Inject
    lateinit var dataManager: DataManager

    private val binding: FragmentProfileBinding by Inflate(R.layout.fragment_profile)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
