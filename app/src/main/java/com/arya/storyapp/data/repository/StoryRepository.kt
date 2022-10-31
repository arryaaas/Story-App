package com.arya.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.arya.storyapp.data.Result
import com.arya.storyapp.data.StoryRemoteMediator
import com.arya.storyapp.data.local.database.StoryDatabase
import com.arya.storyapp.data.local.entity.StoryEntity
import com.arya.storyapp.data.remote.response.PostNewStoryResponse
import com.arya.storyapp.data.remote.response.CustomErrorResponse
import com.arya.storyapp.data.remote.response.GetAllStoryResponse
import com.arya.storyapp.data.remote.retrofit.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class StoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
) {

    fun postNewStory(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): LiveData<Result<PostNewStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postNewStory(token, photo, description, lat, lon)
            emit(Result.Success(response))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val type = object : TypeToken<CustomErrorResponse>() {}.type
                    val customErrorResponse: CustomErrorResponse? =
                        Gson().fromJson(e.response()?.errorBody()?.charStream(), type)
                    emit(Result.Error(customErrorResponse?.message.toString()))
                }
                is IOException -> {
                    emit(Result.Error("Please check your network connection"))
                }
                else -> {
                    emit(Result.Error("An unexpected error occurred"))
                }
            }
        }
    }

    fun getAllStory(
        token: String
    ): LiveData<PagingData<StoryEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = { storyDatabase.storyDao().getAllStory() }
        ).liveData
    }

    fun getAllStoryWithLocation(
        token: String,
        location: Int
    ): LiveData<Result<GetAllStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllStory(token = token, location = location)
            emit(Result.Success(response))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val type = object : TypeToken<CustomErrorResponse>() {}.type
                    val customErrorResponse: CustomErrorResponse? =
                        Gson().fromJson(e.response()?.errorBody()?.charStream(), type)
                    emit(Result.Error(customErrorResponse?.message.toString()))
                }
                is IOException -> {
                    emit(Result.Error("Please check your network connection"))
                }
                else -> {
                    emit(Result.Error("An unexpected error occurred"))
                }
            }
        }
    }
}