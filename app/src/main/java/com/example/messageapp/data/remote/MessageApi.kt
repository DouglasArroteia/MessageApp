package com.example.messageapp.data.remote

import retrofit2.http.GET

interface MessageApi {

    @GET("conversation")
    suspend fun getConversation(): MessageListDTO
}