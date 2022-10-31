package com.arya.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arya.storyapp.data.Result
import com.arya.storyapp.data.remote.response.PostRegisterResponse
import com.arya.storyapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _postRegisterResult = MutableLiveData<Result<PostRegisterResponse>>()
    val postRegisterResult: LiveData<Result<PostRegisterResponse>> = _postRegisterResult

    fun postRegister(
        name: String,
        email: String,
        password: String
    ) {
        authRepository.postRegister(name, email, password).observeForever {
            _postRegisterResult.value = it
        }
    }
}