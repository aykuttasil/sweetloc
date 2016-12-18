package com.aykuttasil.sweetloc.util.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.model.ModelUserTracker;
import com.aykuttasil.sweetloc.util.PicassoCircleTransform;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by aykutasil on 12.12.2016.
 */

public class UserTrackerListAdapter extends RecyclerView.Adapter<UserTrackerListAdapter.UserTrackerViewHolder> {

    private Context mContext;
    private List<ModelUserTracker> mList;

    @DebugLog
    public UserTrackerListAdapter(Context context, List<ModelUserTracker> list) {
        this.mContext = context;
        this.mList = list;
    }

    @DebugLog
    @Override
    public UserTrackerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(mContext).inflate(R.layout.listitem_user_tracker, parent, false);
        return new UserTrackerViewHolder(vi);
    }

    @DebugLog
    @Override
    public void onBindViewHolder(UserTrackerViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @DebugLog
    @Override
    public int getItemCount() {
        return mList.size();
    }

    @DebugLog
    public void addItem(ModelUserTracker modelUserTracker) {
        mList.add(modelUserTracker);
        notifyDataSetChanged();
    }

    public void removeItem(ModelUserTracker modelUserTracker) {
        mList.remove(modelUserTracker);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    class UserTrackerViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageViewProfilePicture;
        TextView mTextViewAdSoyad;
        TextView mTextViewEmail;
        //SeekBar mSeekBarTracker;

        UserTrackerViewHolder(View itemView) {
            super(itemView);
            mImageViewProfilePicture = (ImageView) itemView.findViewById(R.id.ImageViewProfilePicture);
            //mTextViewAdSoyad = (TextView) itemView.findViewById(R.id.TextViewAdSoyad);
            mTextViewEmail = (TextView) itemView.findViewById(R.id.TextViewEmail);
            //mSeekBarTracker = (SeekBar) itemView.findViewById(R.id.SeekBarTracker);
        }

        @DebugLog
        void bind(ModelUserTracker modelUserTracker) {

            if (modelUserTracker.getProfilePictureUrl() != null && !modelUserTracker.getProfilePictureUrl().isEmpty()) {

                Logger.i("Picture Url: " + modelUserTracker.getProfilePictureUrl());

                Picasso.with(mContext)
                        .load(modelUserTracker.getProfilePictureUrl())
                        .transform(new PicassoCircleTransform())
                        .into(mImageViewProfilePicture);

            } else {
                mImageViewProfilePicture.setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.ic_account_circle_light_blue_300_24dp));
            }

            mTextViewEmail.setText(modelUserTracker.getEmail());


            //mTextViewAdSoyad.setText(modelUserTracker.getAd() + " " + modelUserTracker.getSoyAd());
            /*mSeekBarTracker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b) {
                        Toast.makeText(itemView.getContext(), "true", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            */
        }
    }
}