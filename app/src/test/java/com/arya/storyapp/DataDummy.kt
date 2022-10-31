package com.arya.storyapp

import com.arya.storyapp.data.local.SessionModel
import com.arya.storyapp.data.local.entity.StoryEntity
import com.arya.storyapp.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {

    fun generateDummyName(): String {
        return "user"
    }

    fun generateDummyEmail(): String {
        return "user@example.com"
    }

    fun generateDummyPassword(): String {
        return "123456"
    }

    fun generateDummyPostRegisterResponse(): PostRegisterResponse {
        return PostRegisterResponse(
            false,
            "User Created"
        )
    }

    fun generateDummyPostLoginResponse(): PostLoginResponse {
        val loginResult = LoginResult(
            "user-yj5pc_LARC_AgK61",
            "Arif Faizin",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
        )

        return PostLoginResponse(
            false,
            "success",
            loginResult
        )
    }

    fun generateDummyError(): String {
        return "An unexpected error occurred"
    }

    fun generateDummyToken(): String {
        return "token"
    }

    fun generateDummySessionModel(): SessionModel {
        return SessionModel(
            "user",
            "token",
            true
        )
    }

    fun generateDummySessionModelEmpty(): SessionModel {
        return SessionModel(
            "",
            "",
            false
        )
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "dummy"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyRequestBody(): RequestBody {
        val dummyText = "dummy"
        return dummyText.toRequestBody()
    }

    fun generateDummyPostNewStoryResponse(): PostNewStoryResponse {
        return PostNewStoryResponse(
            false,
            "Success"
        )
    }

    fun generateDummyStoryEntity(): List<StoryEntity> {
        val storyList = ArrayList<StoryEntity>()
        for (i in 0..10) {
            val story = StoryEntity(
                "story-Yk89TWuHxSYsE-1u",
                "Dicoding",
                "Because for me, in this tough times, Bangkit represents hope. Only in Bangkit..a person like me..can study with the best curriculum and best experts. Thanks to Bangkit, now I aspire to be a machine learning specialist!",
                "https://story-api.dicoding.dev/images/stories/photos-1648720737599_XbEPxqHS.jpg",
                "2022-03-31T09:58:57.600Z",
                -7.4730525,
                110.1825048
            )
            storyList.add(story)
        }
        return storyList
    }

    fun generateDummyGetAllStoryResponse(): GetAllStoryResponse {
        val listStoryItem = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val story = ListStoryItem(
                "story-Yk89TWuHxSYsE-1u",
                "Dicoding",
                "Because for me, in this tough times, Bangkit represents hope. Only in Bangkit..a person like me..can study with the best curriculum and best experts. Thanks to Bangkit, now I aspire to be a machine learning specialist!",
                "https://story-api.dicoding.dev/images/stories/photos-1648720737599_XbEPxqHS.jpg",
                "2022-03-31T09:58:57.600Z",
                -7.4730525,
                110.1825048
            )
            listStoryItem.add(story)
        }

        return GetAllStoryResponse(
            false,
        "Stories fetched successfully",
            listStoryItem
        )
    }
}