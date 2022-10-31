package com.arya.storyapp.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.arya.storyapp.DataDummy
import com.arya.storyapp.MainDispatcherRule
import com.arya.storyapp.data.Result
import com.arya.storyapp.data.remote.response.PostRegisterResponse
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
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var registerViewModel: RegisterViewModel
    private val dummyName = DataDummy.generateDummyName()
    private val dummyEmail = DataDummy.generateDummyEmail()
    private val dummyPassword = DataDummy.generateDummyPassword()
    private val dummyError = DataDummy.generateDummyError()
    private val dummyRegister = DataDummy.generateDummyPostRegisterResponse()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(authRepository)
    }

    @Test
    fun `when Register Success Should Not Null and Return Success`() = runTest {
        val expectedRegister = MutableLiveData<Result<PostRegisterResponse>>()
        expectedRegister.value = Result.Success(dummyRegister)

        `when`(authRepository.postRegister(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedRegister)

        registerViewModel.postRegister(dummyName, dummyEmail, dummyPassword)
        advanceUntilIdle()
        val actualRegister = registerViewModel.postRegisterResult.getOrAwaitValue()

        verify(authRepository).postRegister(dummyName, dummyEmail, dummyPassword)

        assertNotNull(actualRegister)
        assertTrue(actualRegister is Result.Success)
    }

    @Test
    fun `when Register Error Should Return Error`() = runTest {
        val expectedRegister = MutableLiveData<Result<PostRegisterResponse>>()
        expectedRegister.value = Result.Error(dummyError)

        `when`(authRepository.postRegister(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedRegister)

        registerViewModel.postRegister(dummyName, dummyEmail, dummyPassword)
        advanceUntilIdle()
        val actualRegister = registerViewModel.postRegisterResult.getOrAwaitValue()

        verify(authRepository).postRegister(dummyName, dummyEmail, dummyPassword)

        assertNotNull(actualRegister)
        assertTrue(actualRegister is Result.Error)
    }
}