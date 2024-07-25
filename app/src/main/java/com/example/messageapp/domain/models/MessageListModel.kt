package com.example.messageapp.domain.models

data class MessageListModel(
    val messages: List<MessageModel>,
    val users: List<UserModel>
)

data class MessageModel(
    val attachments: List<AttachmentModel>?,
    val content: String,
    val id: Int,
    val userId: Int
)

data class UserModel(
    val avatarId: String,
    val id: Int,
    val name: String
)

data class AttachmentModel(
    val id: String,
    val thumbnailUrl: String,
    val title: String,
    val url: String
)