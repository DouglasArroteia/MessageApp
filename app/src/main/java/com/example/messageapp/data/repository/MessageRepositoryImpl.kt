package com.example.messageapp.data.repository

import com.example.messageapp.data.remote.MessageApi
import com.example.messageapp.data.remote.MessageListDTO
import com.example.messageapp.domain.repository.MessageRepository
import com.example.messageapp.util.Resource
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val api: MessageApi,
) : MessageRepository {

    override suspend fun getMessages(): Resource<MessageListDTO> {
        val response = try {
            api.getConversation()
        } catch(e: Exception) {
            return Resource.Error("An unknown error occurred.")
        }
        return Resource.Success(response)
    }
}