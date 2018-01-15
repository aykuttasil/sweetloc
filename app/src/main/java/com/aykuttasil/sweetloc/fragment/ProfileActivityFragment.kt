package com.aykuttasil.sweetloc.fragment

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.databinding.FragmentProfileBinding
import com.aykuttasil.sweetloc.db.DbManager
import com.aykuttasil.sweetloc.model.ModelLocation
import com.aykuttasil.sweetloc.model.ModelUser
import com.aykuttasil.sweetloc.util.delegates.Inflate
import com.google.firebase.database.FirebaseDatabase
import hugo.weaving.DebugLog
import kotlinx.android.synthetic.main.fragment_profile.*

open class ProfileActivityFragment : BaseFragment() {

    private val binding: FragmentProfileBinding by Inflate(R.layout.fragment_profile)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding.modelUser = ModelUser()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInformation()
        TextViewEmail.movementMethod = ScrollingMovementMethod()
    }

    private fun deleteAllRows() {
        FirebaseDatabase.getInstance().reference
                .child(ModelUser::class.java.simpleName)
                .removeValue()

        FirebaseDatabase.getInstance().reference
                .child(ModelLocation::class.java.simpleName)
                .removeValue()
    }

    @DebugLog
    private fun setInformation() {
        val modelUser = DbManager.getModelUser()
        binding.modelUser = modelUser

        /*
        TextViewEmail.text = modelUser.email
        Picasso.with(context)
                .load(modelUser.imageUrl)
                .transform(PicassoCircleTransform())
                .into(ImageViewProfilePicture)
                */
    }

}
