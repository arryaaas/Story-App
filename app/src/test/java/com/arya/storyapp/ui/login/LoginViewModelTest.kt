package com.arya.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.arya.storyapp.DataDummy
import com.arya.storyapp.MainDispatcherRule
import com.arya.storyapp.data.Result
import com.arya.storyapp.data.local.SessionDataStore
import com.arya.storyapp.data.remote.response.PostLoginResponse
import com.arya.storyapp.data.repository.AuthRepository
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
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var sessionDataStore: SessionDataStore

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var loginViewModel: LoginViewModel
    private val dummyEmail = DataDummy.generateDummyEmail()
    private val dummyPassword = DataDummy.generateDummyPassword()
    private val dummyError = DataDummy.generateDummyError()
    private val dummyLogin = DataDummy.generateDummyPostLoginResponse()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(sessionDataStore, authRepository)
    }

    @Test
    fun `when Login Success Should Not Null and Return Success`() = runTest {
        val expectedLogin = MutableLiveData<Result<PostLoginResponse>>()
        expectedLogin.value = Result.Success(dummyLogin)

        `when`(authRepository.postLogin(dummyEmail, dummyPassword)).thenReturn(expectedLogin)

        loginViewModel.postLogin(dummyEmail, dummyPassword)
        advanceUntilIdle()
        val actualLogin = loginViewModel.postLoginResult.getOrAwaitValue()

        verify(authRepository).postLogin(dummyEmail, dummyPassword)

        assertNotNull(actualLogin)
        assertTrue(actualLogin is Result.Success)
    }

    @Test
    fun `when Login Error Should Return Error`() = runTest {
        val expectedLogin = MutableLiveData<Result<PostLoginResponse>>()
        expectedLogin.value = Result.Error(dummyError)

        `when`(authRepository.postLogin(dummyEmail, dummyPassword)).thenReturn(expectedLogin)

        loginViewModel.postLogin(dummyEmail, dummyPassword)
        advanceUntilIdle()
        val actualLogin = loginViewModel.postLoginResult.getOrAwaitValue()

        verify(authRepository).postLogin(dummyEmail, dummyPassword)

        assertNotNull(actualLogin)
        assertTrue(actualLogin is Result.Error)
    }
}