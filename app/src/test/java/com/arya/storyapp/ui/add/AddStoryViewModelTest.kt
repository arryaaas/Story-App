package com.arya.storyapp.ui.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.arya.storyapp.DataDummy
import com.arya.storyapp.MainDispatcherRule
import com.arya.storyapp.data.Result
import com.arya.storyapp.data.local.SessionDataStore
import com.arya.storyapp.data.remote.response.PostNewStoryResponse
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
class AddStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var sessionDataStore: SessionDataStore

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var addStoryViewModel: AddStoryViewModel
    private val dummyToken = DataDummy.generateDummyToken()
    private val dummyError = DataDummy.generateDummyError()
    private val dummyPostNewStoryResponse = DataDummy.generateDummyPostNewStoryResponse()
    private val dummyMultipartFile = DataDummy.generateDummyMultipartFile()
    private val dummyRequestBody = DataDummy.generateDummyRequestBody()

    @Before
    fun setUp() {
        addStoryViewModel = AddStoryViewModel(sessionDataStore, storyRepository)
    }

    @Test
    fun `when Add New Story Success Should Not Null and Return Success`() = runTest {
        val expectedAddNewStory = MutableLiveData<Result<PostNewStoryResponse>>()
        expectedAddNewStory.value = Result.Success(dummyPostNewStoryResponse)

        `when`(storyRepository.postNewStory(dummyToken, dummyMultipartFile, dummyRequestBody, null, null)).thenReturn(expectedAddNewStory)

        addStoryViewModel.postNewStory(dummyToken, dummyMultipartFile, dummyRequestBody, null, null)
        advanceUntilIdle()
        val actualAddNewStory = addStoryViewModel.postNewStoryResult.getOrAwaitValue()

        verify(storyRepository).postNewStory(dummyToken, dummyMultipartFile, dummyRequestBody, null, null)

        assertNotNull(actualAddNewStory)
        assertTrue(actualAddNewStory is Result.Success)
    }

    @Test
    fun `when Add New Story Error Should Return Error`() = runTest {
        val expectedAddNewStory = MutableLiveData<Result<PostNewStoryResponse>>()
        expectedAddNewStory.value = Result.Error(dummyError)

        `when`(storyRepository.postNewStory(dummyToken, dummyMultipartFile, dummyRequestBody, null, null)).thenReturn(expectedAddNewStory)

        addStoryViewModel.postNewStory(dummyToken, dummyMultipartFile, dummyRequestBody, null, null)
        advanceUntilIdle()
        val actualAddNewStory = addStoryViewModel.postNewStoryResult.getOrAwaitValue()

        verify(storyRepository).postNewStory(dummyToken, dummyMultipartFile, dummyRequestBody, null, null)

        assertNotNull(actualAddNewStory)
        assertTrue(actualAddNewStory is Result.Error)
    }
}