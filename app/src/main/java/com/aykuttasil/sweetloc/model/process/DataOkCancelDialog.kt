package com.aykuttasil.sweetloc.model.process

import android.graphics.Color
import android.graphics.drawable.Drawable

/**
 * Created by aykutasil on 1.02.2018.
 */
data class DataOkCancelDialog(var title: String,
                              var content: String,
                              var icon: Drawable? = null,
                              var actionOkText: String? = "Ok",
                              var actionCancelText: String? = "Cancel",
                              var actionOkColor: Int? = Color.BLUE,
                              var actionCancelColor: Int? = Color.GRAY,
                              var actionOk: () -> Unit,
                              var actionCancel: () -> Unit)