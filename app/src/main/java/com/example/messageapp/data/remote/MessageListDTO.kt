package com.example.messageapp.data.remote

data class MessageListDTO(
    val messages: List<MessageDTO>,
    val users: List<UserDTO>
)

data class MessageDTO(
    val attachments: List<AttachmentDTO>?,
    val content: String,
    val id: Int,
    val userId: Int
)

data class UserDTO(
    val avatarId: String,
    val id: Int,
    val name: String
)

data class AttachmentDTO(
    val id: String,
    val thumbnailUrl: String,
    val title: String,
    val url: String
)