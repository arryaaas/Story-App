package com.arya.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arya.storyapp.data.local.SessionDataStore
import com.arya.storyapp.data.local.SessionModel
import com.arya.storyapp.data.local.entity.StoryEntity
import com.arya.storyapp.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionDataStore: SessionDataStore,
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _retrieveSessionResult = MutableLiveData<SessionModel>()
    val retrieveSessionResult: LiveData<SessionModel> = _retrieveSessionResult

    private val _getAllStoryResult = MutableLiveData<PagingData<StoryEntity>>()
    val getAllStoryResult: LiveData<PagingData<StoryEntity>> = _getAllStoryResult

    fun retrieveSession() {
        sessionDataStore.retrieveSession().observeForever {
            _retrieveSessionResult.value = it
        }
    }

    fun getAllStory(
        token: String
    ) {
        storyRepository.getAllStory(token).cachedIn(viewModelScope).observeForever {
            _getAllStoryResult.value = it
        }
    }
}