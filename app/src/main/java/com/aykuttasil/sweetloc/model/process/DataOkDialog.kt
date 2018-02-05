package com.aykuttasil.sweetloc.model.process

import android.graphics.drawable.Drawable

/**
 * Created by aykutasil on 1.02.2018.
 */
data class DataOkDialog(var title: String, var content: String, var icon: Drawable? = null, var action: () -> Unit)