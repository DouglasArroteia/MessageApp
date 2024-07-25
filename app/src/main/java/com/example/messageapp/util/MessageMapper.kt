package com.example.messageapp.util

import com.example.messageapp.data.remote.MessageListDTO
import com.example.messageapp.domain.models.AttachmentModel
import com.example.messageapp.domain.models.MessageListModel
import com.example.messageapp.domain.models.MessageModel
import com.example.messageapp.domain.models.UserModel

fun MessageListDTO.toModel(): MessageListModel {
    with(this) {
        val messageModels = messages.map { messageDTO ->
            MessageModel(
                attachments = messageDTO.attachments?.map { attachmentDTO ->
                    AttachmentModel(
                        id = attachmentDTO.id,
                        thumbnailUrl = attachmentDTO.thumbnailUrl,
                        title = attachmentDTO.title,
                        url = attachmentDTO.url
                    )
                },
                content = messageDTO.content,
                id = messageDTO.id,
                userId = messageDTO.userId
            )
        }

        val userModels = users.map { userDTO ->
            UserModel(
                avatarId = userDTO.avatarId,
                id = userDTO.id,
                name = userDTO.name
            )
        }

        return MessageListModel(
            messages = messageModels,
            users = userModels
        )
    }
}