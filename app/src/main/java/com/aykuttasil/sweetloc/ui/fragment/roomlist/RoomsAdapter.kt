package com.aykuttasil.sweetloc.ui.fragment.roomlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aykuttasil.sweetloc.R
import com.aykuttasil.sweetloc.data.RoomEntity
import com.aykuttasil.sweetloc.databinding.ListitemRoomBinding
import com.aykuttasil.sweetloc.util.BindableAdapter

class RoomsAdapter : ListAdapter<RoomEntity, RoomsAdapter.MyViewHolder>(RoomsAdapter.DIFF_CALLBACK), BindableAdapter<RoomEntity> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val vi: ListitemRoomBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.listitem_room, parent, false)
        return MyViewHolder(vi)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position));
    }

    override fun setData(items: List<RoomEntity>) {
        submitList(items)
    }

    inner class MyViewHolder(val binding: ListitemRoomBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(room: RoomEntity) {
            binding.room = room
            binding.root.setOnClickListener {
                Toast.makeText(itemView.context, room.roomName, Toast.LENGTH_SHORT).show()
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RoomEntity>() {
            override fun areItemsTheSame(oldItem: RoomEntity, newItem: RoomEntity): Boolean {
                return oldItem.roomName == newItem.roomName
            }

            override fun areContentsTheSame(oldItem: RoomEntity, newItem: RoomEntity): Boolean {
                return oldItem.roomName == newItem.roomName
            }

        }
    }
}