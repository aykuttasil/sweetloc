/* Author - Aykut Asil(aykuttasil) */
package com.aykuttasil.sweetloc.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.aykuttasil.sweetloc.R

class CwProgress @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {

    private val progressBar: ProgressBar
    private val textView: TextView

    init {
        val vi = inflate(context, R.layout.progress_dialog_fragment_layout, this)

        progressBar = vi.findViewById(R.id.progressBar)
        textView = vi.findViewById(R.id.textView)
    }
}