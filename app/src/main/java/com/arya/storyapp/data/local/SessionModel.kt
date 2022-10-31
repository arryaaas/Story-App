package com.arya.storyapp.data.local

data class SessionModel(
    val name: String,
    val token: String,
    val isLogin: Boolean,
)