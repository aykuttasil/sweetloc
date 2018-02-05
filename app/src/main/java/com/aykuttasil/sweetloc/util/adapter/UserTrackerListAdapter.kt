package com.aykuttasil.sweetloc.util.adapter

import android.content.Context
import android.media.AudioManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.app.Const
import com.aykuttasil.sweetloc.data.DataManager
import com.aykuttasil.sweetloc.data.local.entity.UserTrackerEntity
import com.aykuttasil.sweetloc.helper.SuperHelper
import com.aykuttasil.sweetloc.util.PicassoCircleTransform
import com.orhanobut.logger.Logger
import com.squareup.picasso.Picasso
import hugo.weaving.DebugLog
import org.jetbrains.anko.audioManager
import javax.inject.Inject

class UserTrackerListAdapter @Inject constructor(val mContext: Context, val dataManager: DataManager) : RecyclerView.Adapter<UserTrackerListAdapter.UserTrackerViewHolder>() {

    private val mList = arrayListOf<UserTrackerEntity>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserTrackerViewHolder {
        val vi = LayoutInflater.from(mContext).inflate(R.layout.listitem_user_tracker, parent, false)
        return UserTrackerViewHolder(vi)
    }

    override fun onBindViewHolder(holder: UserTrackerViewHolder?, position: Int) {
        holder?.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun addItem(modelUserTracker: UserTrackerEntity) {
        mList.add(modelUserTracker)
        notifyDataSetChanged()
    }

    fun addItemList(list: List<UserTrackerEntity>) {
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearAndAddItemList(list: List<UserTrackerEntity>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun removeItem(modelUserTracker: UserTrackerEntity) {
        mList.remove(modelUserTracker)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        mList.removeAt(position)
        notifyDataSetChanged()
    }

    inner class UserTrackerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mImageViewProfilePicture: ImageView
        var mTextViewAdSoyad: TextView? = null
        var mTextViewEmail: TextView
        var mButtonWakeUp: Button

        init {
            mImageViewProfilePicture = itemView.findViewById<View>(R.id.ImageViewProfilePicture) as ImageView
            mTextViewEmail = itemView.findViewById<View>(R.id.TextViewEmail) as TextView
            mButtonWakeUp = itemView.findViewById<View>(R.id.ButtonWakeUp) as Button
        }

        fun bind(modelUserTracker: UserTrackerEntity) {
            if (!modelUserTracker.profilePictureUrl.isNullOrEmpty()) {
                Logger.i("Picture Url: " + modelUserTracker.profilePictureUrl)
                Picasso.with(mContext)
                        .load(modelUserTracker.profilePictureUrl)
                        .transform(PicassoCircleTransform())
                        .into(mImageViewProfilePicture)
            } else {
                mImageViewProfilePicture.setImageDrawable(itemView.context.resources.getDrawable(R.drawable.ic_account_circle_light_blue_300_24dp))
            }
            mTextViewEmail.text = modelUserTracker.email
            mButtonWakeUp.setOnClickListener { v ->
                Logger.i("mButtonWakeUp click")
                SuperHelper.Companion.sendNotif(Const.ACTION_PHONE_UNMUTE, dataManager)

                val audioManager = mContext.audioManager
                val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND)
            }
        }
    }
}
