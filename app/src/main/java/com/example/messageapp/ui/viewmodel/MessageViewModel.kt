package com.example.messageapp.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messageapp.domain.models.MessageListModel
import com.example.messageapp.domain.models.MessageModel
import com.example.messageapp.domain.models.UserModel
import com.example.messageapp.domain.repository.MessageRepository
import com.example.messageapp.util.Resource
import com.example.messageapp.util.SharedPreferencesHelper
import com.example.messageapp.util.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val repository: MessageRepository,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    var messagesList = mutableStateOf<List<MessageModel>>(listOf())
    var usersList = mutableStateOf<List<UserModel>>(listOf())

    init {
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            sharedPreferencesHelper.retrieveMessages()?.let { messages ->
                usersList.value = messages.users
                messagesList.value = messages.messages
            } ?: run {
                when (val result = repository.getMessages()) {
                    is Resource.Success -> {
                        sharedPreferencesHelper.addMessages(result.data?.toModel())
                        usersList.value = result.data?.toModel()?.users ?: listOf()
                        messagesList.value = result.data?.toModel()?.messages ?: listOf()
                    }

                    is Resource.Error -> {
                        result.message
                    }
                }
            }
        }
    }
}