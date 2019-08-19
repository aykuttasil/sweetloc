/* Author - Aykut Asil(@aykuttasil) */
package com.aykuttasil.sweetloc.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.aykuttasil.sweetloc.R

class ProgressDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.progress_dialog_fragment_layout, container, false)
    }
}
