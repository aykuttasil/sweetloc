package com.aykuttasil.sweetloc.ui.fragment.roommemberlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.data.local.entity.UserEntity
import com.aykuttasil.sweetloc.databinding.ListitemRoommemberBinding
import com.aykuttasil.sweetloc.util.BindableAdapter

class RoomMembersAdapter : ListAdapter<UserEntity, RoomMembersAdapter.MyViewHolder>(RoomMembersAdapter.DIFF_CALLBACK), BindableAdapter<UserEntity> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val vi: ListitemRoommemberBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.listitem_roommember, parent, false)
        return MyViewHolder(vi)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position));
    }

    override fun setData(items: List<UserEntity>) {
        submitList(items)
    }

    inner class MyViewHolder(val binding: ListitemRoommemberBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserEntity) {
            binding.user = user
            binding.root.setOnClickListener {
                Toast.makeText(itemView.context, user.userName, Toast.LENGTH_SHORT).show()
            }
            binding.executePendingBindings()
            /*
            mButtonWakeUp.setOnClickListener {
                Logger.i("mButtonWakeUp click")

                // SweetLocHelper.sendNotif(Const.ACTION_PHONE_UNMUTE, userRepository, userTrackerRepository)

                val audioManager = mContext.audioManager
                val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume,
                        AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND)
            }
             */
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserEntity>() {
            override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                return oldItem.userName == newItem.userName
            }

        }
    }
}


