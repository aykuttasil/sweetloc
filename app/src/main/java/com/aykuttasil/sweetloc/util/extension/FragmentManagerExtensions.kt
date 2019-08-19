/* Author - Aykut Asil(@aykuttasil) */
package com.aykuttasil.sweetloc.util.extension

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun FragmentManager.transaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}
