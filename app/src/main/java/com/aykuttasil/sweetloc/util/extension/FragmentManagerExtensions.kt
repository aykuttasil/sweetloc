package com.aykuttasil.sweetloc.util.extension

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


inline fun androidx.fragment.app.FragmentManager.transaction(func: androidx.fragment.app.FragmentTransaction.() -> androidx.fragment.app.FragmentTransaction) {
    beginTransaction().func().commit()
}