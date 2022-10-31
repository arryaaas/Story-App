package com.arya.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.storyapp.data.Result
import com.arya.storyapp.data.local.SessionDataStore
import com.arya.storyapp.data.local.SessionModel
import com.arya.storyapp.data.remote.response.PostLoginResponse
import com.arya.storyapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionDataStore: SessionDataStore,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _retrieveSessionResult = MutableLiveData<SessionModel>()
    val retrieveSessionResult: LiveData<SessionModel> = _retrieveSessionResult

    private val _postLoginResult = MutableLiveData<Result<PostLoginResponse>>()
    val postLoginResult: LiveData<Result<PostLoginResponse>> = _postLoginResult

    fun retrieveSession() {
        sessionDataStore.retrieveSession().observeForever {
            _retrieveSessionResult.value = it
        }
    }

    fun saveSession(sessionModel: SessionModel) {
        viewModelScope.launch {
            sessionDataStore.saveSession(sessionModel)
        }
    }

    fun postLogin(
        email: String,
        password: String
    ) {
        authRepository.postLogin(email, password).observeForever {
            _postLoginResult.value = it
        }
    }
}