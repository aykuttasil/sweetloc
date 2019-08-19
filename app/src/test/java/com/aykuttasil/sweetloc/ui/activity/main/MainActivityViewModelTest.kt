/* Author - Aykut Asil(@aykuttasil) */
package com.aykuttasil.sweetloc.ui.activity.main

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aykuttasil.sweetloc.data.repository.LocationRepository
import com.aykuttasil.sweetloc.data.repository.RoomRepository
import com.aykuttasil.sweetloc.data.repository.UserRepository
import com.aykuttasil.sweetloc.helper.SweetLocHelper
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityViewModelTest {

    @MockK
    lateinit var mockUserRepository: UserRepository

    @MockK
    lateinit var mockLocationRepository: LocationRepository

    @MockK
    lateinit var mockRoomRepository: RoomRepository

    @MockK
    lateinit var mockSweetLocHelper: SweetLocHelper

    lateinit var viewModel: MainActivityViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        viewModel = MainActivityViewModel(
            ApplicationProvider.getApplicationContext(),
            mockUserRepository,
            mockLocationRepository,
            mockRoomRepository,
            mockSweetLocHelper
        )
    }

    @After
    fun tearDown() {
    }
}
