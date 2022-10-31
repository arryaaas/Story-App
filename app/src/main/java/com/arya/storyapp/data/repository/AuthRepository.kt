package com.arya.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.arya.storyapp.data.Result
import com.arya.storyapp.data.remote.response.CustomErrorResponse
import com.arya.storyapp.data.remote.response.PostLoginResponse
import com.arya.storyapp.data.remote.response.PostRegisterResponse
import com.arya.storyapp.data.remote.retrofit.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun postLogin(
        email: String,
        password: String
    ): LiveData<Result<PostLoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postLogin(email, password)
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

    fun postRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<PostRegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.postRegister(name, email, password)
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