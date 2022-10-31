package com.arya.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.arya.storyapp.*
import com.arya.storyapp.adapter.StoryAdapter
import com.arya.storyapp.data.local.SessionDataStore
import com.arya.storyapp.data.local.SessionModel
import com.arya.storyapp.data.local.entity.StoryEntity
import com.arya.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.Dispatchers
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

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var sessionDataStore: SessionDataStore

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var homeViewModel: HomeViewModel
    private val dummyToken = DataDummy.generateDummyToken()
    private val dummySessionModel = DataDummy.generateDummySessionModel()
    private val dummyStoryEntity = DataDummy.generateDummyStoryEntity()

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(sessionDataStore, storyRepository)
    }

    @Test
    fun `when User Already Logged In Should Return Token`() = runTest {
        val expectedSession = MutableLiveData<SessionModel>()
        expectedSession.value = dummySessionModel

        `when`(sessionDataStore.retrieveSession()).thenReturn(expectedSession)

        homeViewModel.retrieveSession()
        advanceUntilIdle()
        val actualSession = homeViewModel.retrieveSessionResult.getOrAwaitValue()

        verify(sessionDataStore).retrieveSession()

        assertNotNull(actualSession)
        assertEquals(dummySessionModel.token, actualSession.token)
    }

    @Test
    fun `when Get All Story Success Should Not Null and Return Success`() = runTest {
        val storyPagingData = StoryPagingSource.snapshot(dummyStoryEntity)

        val expectedStoryPagingData = MutableLiveData<PagingData<StoryEntity>>()
        expectedStoryPagingData.value = storyPagingData

        `when`(storyRepository.getAllStory(dummyToken)).thenReturn(expectedStoryPagingData)

        homeViewModel.getAllStory(dummyToken)
        advanceUntilIdle()
        val actualStoryPagingData = homeViewModel.getAllStoryResult.getOrAwaitValue()

        verify(storyRepository).getAllStory(dummyToken)

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = NoopListUpdateCallback(),
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStoryPagingData)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStoryEntity, differ.snapshot())
        assertEquals(dummyStoryEntity.size, differ.snapshot().size)
        assertEquals(dummyStoryEntity[0].id, differ.snapshot()[0]?.id)
    }
}