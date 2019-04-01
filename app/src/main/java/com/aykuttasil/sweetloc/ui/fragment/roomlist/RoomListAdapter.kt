package com.aykuttasil.sweetloc.ui.fragment.roomlist

import android.content.Context
import android.media.AudioManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.data.RoomEntity
import com.aykuttasil.sweetloc.data.repository.RoomRepository
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.util.BindableAdapter
import com.orhanobut.logger.Logger
import org.jetbrains.anko.audioManager
import javax.inject.Inject

class RoomListAdapter @Inject constructor(
        val mContext: Context,
        private val userRepository: UserRepository,
        private val roomRepository: RoomRepository
) : RecyclerView.Adapter<RoomListAdapter.RoomViewHolder>(), BindableAdapter<RoomEntity> {

    private val mList = arrayListOf<RoomEntity>()

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RoomViewHolder {
        val vi = LayoutInflater.from(mContext).inflate(R.layout.listitem_room, parent, false)
        return RoomViewHolder(vi)
    }

    override fun onBindViewHolder(
            holder: RoomViewHolder,
            position: Int
    ) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun setData(items: List<RoomEntity>) {
        addItemList(items)
    }

    fun addItem(room: RoomEntity) {
        mList.add(room)
        notifyDataSetChanged()
    }

    fun addItemList(list: List<RoomEntity>?) {
        list?.filter { !mList.contains(it) }?.apply {
            mList.addAll(this)
            notifyDataSetChanged()
        }
    }

    fun clearAndAddItemList(list: List<RoomEntity>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun removeItem(room: RoomEntity) {
        mList.remove(room)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        mList.removeAt(position)
        notifyDataSetChanged()
    }

    inner class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mImageViewProfilePicture: ImageView
        var mTextViewAdSoyad: TextView? = null
        var mTextViewEmail: TextView
        var mButtonWakeUp: Button

        init {
            mImageViewProfilePicture = itemView.findViewById<View>(R.id.ImageViewProfilePicture) as ImageView
            mTextViewEmail = itemView.findViewById<View>(R.id.TextViewEmail) as TextView
            mButtonWakeUp = itemView.findViewById<View>(R.id.ButtonWakeUp) as Button
        }

        fun bind(room: RoomEntity) {
            /*
            if (!room.profilePictureUrl.isNullOrEmpty()) {
                Logger.i("Picture Url: " + room.profilePictureUrl)
                // Picasso.Builder(mContext).build()
                Picasso.get()
                        .load(room.profilePictureUrl)
                        .transform(PicassoCircleTransform())
                        .into(mImageViewProfilePicture)
            } else {
                mImageViewProfilePicture.setImageDrawable(itemView.context.resources.getDrawable(
                        R.drawable.ic_account_circle_light_blue_300_24dp))
            }
             */

            mTextViewEmail.text = room.roomName

            mButtonWakeUp.setOnClickListener {
                Logger.i("mButtonWakeUp click")

                // SweetLocHelper.sendNotif(Const.ACTION_PHONE_UNMUTE, userRepository, userTrackerRepository)

                val audioManager = mContext.audioManager
                val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume,
                        AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND)
            }
        }
    }
}
