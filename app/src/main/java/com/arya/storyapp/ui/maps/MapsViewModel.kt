package com.arya.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arya.storyapp.data.Result
import com.arya.storyapp.data.local.SessionDataStore
import com.arya.storyapp.data.local.SessionModel
import com.arya.storyapp.data.remote.response.GetAllStoryResponse
import com.arya.storyapp.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val sessionDataStore: SessionDataStore,
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _retrieveSessionResult = MutableLiveData<SessionModel>()
    val retrieveSessionResult: LiveData<SessionModel> = _retrieveSessionResult

    private val _getAllStoryWithLocationResult = MutableLiveData<Result<GetAllStoryResponse>>()
    val getAllStoryWithLocationResult : LiveData<Result<GetAllStoryResponse>> = _getAllStoryWithLocationResult

    fun retrieveSession() {
        sessionDataStore.retrieveSession().observeForever {
            _retrieveSessionResult.value = it
        }
    }

    fun getAllStoryWithLocation(
        token: String,
        location: Int
    ) {
        storyRepository.getAllStoryWithLocation(token, location).observeForever {
            _getAllStoryWithLocationResult.value = it
        }
    }
}