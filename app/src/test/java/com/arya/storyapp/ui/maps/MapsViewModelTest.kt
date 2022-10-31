package com.arya.storyapp.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.arya.storyapp.DataDummy
import com.arya.storyapp.MainDispatcherRule
import com.arya.storyapp.data.Result
import com.arya.storyapp.data.local.SessionDataStore
import com.arya.storyapp.data.remote.response.GetAllStoryResponse
import com.arya.storyapp.data.repository.StoryRepository
import com.arya.storyapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
class MapsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var sessionDataStore: SessionDataStore

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var mapsViewModel: MapsViewModel
    private val dummyToken = DataDummy.generateDummyToken()
    private val dummyError = DataDummy.generateDummyError()
    private val dummyStory = DataDummy.generateDummyGetAllStoryResponse()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(sessionDataStore, storyRepository)
    }

    @Test
    fun `when Get All Story With Location Success Should Not Null and Return Success`() = runTest {
        val expectedStory = MutableLiveData<Result<GetAllStoryResponse>>()
        expectedStory.value = Result.Success(dummyStory)

        `when`(storyRepository.getAllStoryWithLocation(dummyToken, 1)).thenReturn(expectedStory)

        mapsViewModel.getAllStoryWithLocation(dummyToken, 1)
        advanceUntilIdle()
        val actualStory = mapsViewModel.getAllStoryWithLocationResult.getOrAwaitValue()

        verify(storyRepository).getAllStoryWithLocation(dummyToken, 1)

        assertNotNull(actualStory)
        assertTrue(actualStory is Result.Success)
    }

    @Test
    fun `when Get All Story With Location Error Should Return Error`() = runTest {
        val expectedStory = MutableLiveData<Result<GetAllStoryResponse>>()
        expectedStory.value = Result.Error(dummyError)

        `when`(storyRepository.getAllStoryWithLocation(dummyToken, 1)).thenReturn(expectedStory)

        mapsViewModel.getAllStoryWithLocation(dummyToken, 1)
        advanceUntilIdle()
        val actualStory = mapsViewModel.getAllStoryWithLocationResult.getOrAwaitValue()

        verify(storyRepository).getAllStoryWithLocation(dummyToken, 1)

        assertNotNull(actualStory)
        assertTrue(actualStory is Result.Error)
    }
}