package com.aykuttasil.sweetloc.ui.activity.main

import android.app.Application
import android.app.Instrumentation
import org.junit.After
import org.junit.Before

class MainActivityViewModelTest {

    lateinit var viewModel: MainActivityViewModel

    @Before
    fun setUp() {
        viewModel = MainActivityViewModel()
    }

    @After
    fun tearDown() {
    }
}