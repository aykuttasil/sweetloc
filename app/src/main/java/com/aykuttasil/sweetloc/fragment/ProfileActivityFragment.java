package com.aykuttasil.sweetloc.fragment;

import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.aykuttasil.sweetloc.R;
import com.aykuttasil.sweetloc.activity.ProfileActivity;
import com.aykuttasil.sweetloc.db.DbManager;
import com.aykuttasil.sweetloc.model.ModelUser;
import com.aykuttasil.sweetloc.util.PicassoCircleTransform;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import hugo.weaving.DebugLog;

@EFragment(R.layout.fragment_profile)
public class ProfileActivityFragment extends BaseFragment {

    @ViewById(R.id.ImageViewProfilePicture)
    ImageView mImageViewProfilePicture;

    @ViewById(R.id.TextViewEmail)
    TextView mTextViewEmail;

    //

    ProfileActivity mActivity;

    @DebugLog
    @AfterViews
    public void initializeAfterViews() {
        mActivity = (ProfileActivity) getActivity();
        setInformation();
        mTextViewEmail.setMovementMethod(new ScrollingMovementMethod());
    }

    @DebugLog
    private void setInformation() {

        ModelUser modelUser = DbManager.getModelUser();

        mTextViewEmail.setText(modelUser.getEmail());

        Picasso.with(getContext())
                .load(modelUser.getImageUrl())
                .transform(new PicassoCircleTransform())
                .into(mImageViewProfilePicture);

    }

}
