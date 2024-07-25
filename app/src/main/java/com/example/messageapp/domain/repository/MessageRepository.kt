package com.example.messageapp.domain.repository

import com.example.messageapp.data.remote.MessageListDTO
import com.example.messageapp.util.Resource

interface MessageRepository {
    suspend fun getMessages(): Resource<MessageListDTO>
}