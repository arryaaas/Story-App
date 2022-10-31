package com.arya.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.arya.storyapp.*
import com.arya.storyapp.adapter.StoryAdapter
import com.arya.storyapp.data.Result
import com.arya.storyapp.data.local.database.StoryDatabase
import com.arya.storyapp.data.local.entity.StoryEntity
import com.arya.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var storyDatabase: StoryDatabase

    @Mock
    private lateinit var storyRepositoryMock: StoryRepository

    private lateinit var storyRepository: StoryRepository
    private val dummyToken = DataDummy.generateDummyToken()
    private val dummyMultipartFile = DataDummy.generateDummyMultipartFile()
    private val dummyRequestBody = DataDummy.generateDummyRequestBody()
    private val dummyStoryEntity = DataDummy.generateDummyStoryEntity()

    @Before
    fun setUp() {
        storyRepository = StoryRepository(apiService, storyDatabase)
    }

    @Test
    fun `when Post New Story Success Should Not Null and Return Success`() = runTest {
        val expectedPostNewStoryResponse = DataDummy.generateDummyPostNewStoryResponse()

        `when`(apiService.postNewStory(dummyToken, dummyMultipartFile, dummyRequestBody, null, null)).thenReturn(expectedPostNewStoryResponse)

        val actualPostNewStoryResponse = storyRepository.postNewStory(dummyToken, dummyMultipartFile, dummyRequestBody, null, null)
        actualPostNewStoryResponse.observeForTesting {
            assertNotNull(actualPostNewStoryResponse)
            assertEquals(
                expectedPostNewStoryResponse.message,
                (actualPostNewStoryResponse.value as Result.Success).data.message
            )
        }
    }

    @Test
    fun `when Get All Story Success Should Not Null and Return Success`() = runTest {
        val storyPagingData = StoryPagingSource.snapshot(dummyStoryEntity)

        val expectedStoryPagingData = MutableLiveData<PagingData<StoryEntity>>()
        expectedStoryPagingData.value = storyPagingData

        `when`(storyRepositoryMock.getAllStory(dummyToken)).thenReturn(expectedStoryPagingData)

        val actualStoryPagingData = storyRepositoryMock.getAllStory(dummyToken).getOrAwaitValue()

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

    @Test
    fun `when Get All Story With Location Success Should Not Null and Return Success`() = runTest {
        val expectedGetAllStoryResponse = DataDummy.generateDummyGetAllStoryResponse()

        `when`(apiService.getAllStory(token = dummyToken, location = 1)).thenReturn(expectedGetAllStoryResponse)

        val actualGetAllStoryResponse = storyRepository.getAllStoryWithLocation(dummyToken, 1)
        actualGetAllStoryResponse.observeForTesting {
            assertNotNull(actualGetAllStoryResponse)
            assertEquals(
                expectedGetAllStoryResponse.listStory.size,
                (actualGetAllStoryResponse.value as Result.Success).data.listStory.size
            )
        }
    }
}