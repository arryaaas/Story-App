package com.arya.storyapp.ui.settings

import androidx.lifecycle.*
import com.arya.storyapp.data.local.SessionDataStore
import com.arya.storyapp.data.local.SessionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sessionDataStore: SessionDataStore
) : ViewModel() {

    private val _retrieveSessionResult = MutableLiveData<SessionModel>()
    val retrieveSessionResult: LiveData<SessionModel> = _retrieveSessionResult

    fun retrieveSession() {
        sessionDataStore.retrieveSession().observeForever {
            _retrieveSessionResult.value = it
        }
    }

    fun deleteSession() {
        viewModelScope.launch {
            sessionDataStore.deleteSession()
        }
    }
}