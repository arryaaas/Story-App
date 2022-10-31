package com.arya.storyapp.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.arya.storyapp.DataDummy
import com.arya.storyapp.MainDispatcherRule
import com.arya.storyapp.data.local.SessionDataStore
import com.arya.storyapp.data.local.SessionModel
import com.arya.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var sessionDataStore: SessionDataStore

    private lateinit var settingsViewModel: SettingsViewModel
    private val dummySession = DataDummy.generateDummySessionModel()
    private val dummySessionEmpty = DataDummy.generateDummySessionModelEmpty()

    @Before
    fun setUp() {
        settingsViewModel = SettingsViewModel(sessionDataStore)
    }

    @Test
    fun `when User Already Logged In Should Return Name`() = runTest {
        val expectedSession = MutableLiveData<SessionModel>()
        expectedSession.value = dummySession

        `when`(sessionDataStore.retrieveSession()).thenReturn(expectedSession)

        settingsViewModel.retrieveSession()
        advanceUntilIdle()
        val actualSession = settingsViewModel.retrieveSessionResult.getOrAwaitValue()

        verify(sessionDataStore).retrieveSession()

        assertNotNull(actualSession)
        assertEquals(dummySession.name, actualSession.name)
    }

    @Test
    fun `when User Not Logged In Should Return Empty String`() = runTest {
        val expectedSession = MutableLiveData<SessionModel>()
        expectedSession.value = dummySessionEmpty

        `when`(sessionDataStore.retrieveSession()).thenReturn(expectedSession)

        settingsViewModel.retrieveSession()
        advanceUntilIdle()
        val actualSession = settingsViewModel.retrieveSessionResult.getOrAwaitValue()

        verify(sessionDataStore).retrieveSession()

        assertNotNull(actualSession)
        assertEquals(dummySessionEmpty.name, actualSession.name)
    }
}