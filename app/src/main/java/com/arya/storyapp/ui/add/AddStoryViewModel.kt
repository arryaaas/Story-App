package com.arya.storyapp.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arya.storyapp.data.Result
import com.arya.storyapp.data.local.SessionDataStore
import com.arya.storyapp.data.local.SessionModel
import com.arya.storyapp.data.remote.response.PostNewStoryResponse
import com.arya.storyapp.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val sessionDataStore: SessionDataStore,
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _retrieveSessionResult = MutableLiveData<SessionModel>()
    val retrieveSessionResult: LiveData<SessionModel> = _retrieveSessionResult

    private val _postNewStoryResult = MutableLiveData<Result<PostNewStoryResponse>>()
    val postNewStoryResult: LiveData<Result<PostNewStoryResponse>> = _postNewStoryResult

    fun retrieveSession() {
        sessionDataStore.retrieveSession().observeForever {
            _retrieveSessionResult.value = it
        }
    }

    fun postNewStory(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) {
        storyRepository.postNewStory(token, photo, description, lat, lon).observeForever {
            _postNewStoryResult.value = it
        }
    }
}