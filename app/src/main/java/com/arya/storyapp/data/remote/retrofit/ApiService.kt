package com.arya.storyapp.data.remote.retrofit

import com.arya.storyapp.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("/v1/register")
    suspend fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): PostRegisterResponse

    @FormUrlEncoded
    @POST("/v1/login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): PostLoginResponse

    @Multipart
    @POST("/v1/stories")
    suspend fun postNewStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): PostNewStoryResponse

    @GET("/v1/stories")
    suspend fun getAllStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): GetAllStoryResponse
}