package com.arya.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.arya.storyapp.DataDummy
import com.arya.storyapp.MainDispatcherRule
import com.arya.storyapp.data.Result
import com.arya.storyapp.data.remote.retrofit.ApiService
import com.arya.storyapp.getOrAwaitValue
import com.arya.storyapp.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var authRepository: AuthRepository
    private val dummyName = DataDummy.generateDummyName()
    private val dummyEmail = DataDummy.generateDummyEmail()
    private val dummyPassword = DataDummy.generateDummyPassword()

    @Before
    fun setUp() {
        authRepository = AuthRepository(apiService)
    }

    @Test
    fun `when Login Success Should Not Null and Return Success`() = runTest {
        val expectedPostLoginResponse = DataDummy.generateDummyPostLoginResponse()

        `when`(apiService.postLogin(dummyEmail, dummyPassword)).thenReturn(expectedPostLoginResponse)

        val actualPostLoginResponse = authRepository.postLogin(dummyEmail, dummyPassword)
        actualPostLoginResponse.observeForTesting {
            assertNotNull(actualPostLoginResponse)
            assertEquals(
                expectedPostLoginResponse.loginResult,
                (actualPostLoginResponse.value as Result.Success).data.loginResult
            )
        }
    }

    @Test
    fun `when Register Success Should Not Null and Return Success`() = runTest {
        val expectedPostRegisterResponse = DataDummy.generateDummyPostRegisterResponse()

        `when`(apiService.postRegister(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedPostRegisterResponse)

        val postRegisterResponse = authRepository.postRegister(dummyName, dummyEmail, dummyPassword)
        postRegisterResponse.observeForTesting {
            val actualPostRegisterResponse = postRegisterResponse.getOrAwaitValue()
            assertNotNull(actualPostRegisterResponse)
            assertEquals(
                expectedPostRegisterResponse.message,
                (actualPostRegisterResponse as Result.Success).data.message
            )
        }
    }
}