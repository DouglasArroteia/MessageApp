package com.example.messageapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.messageapp.domain.models.MessageListModel
import com.google.gson.Gson

/**
 * For an easy communication with the SharedPreferences.
 */
class SharedPreferencesHelper(private val context: Context) {
    private val sharedPrefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    private val editor by lazy {
        sharedPrefs.edit()
    }

    fun addMessages(messages: MessageListModel?) = editor.putString("messages", Gson().toJson(messages)).apply()

    fun retrieveMessages() = Gson().fromJson(sharedPrefs.getString("messages", ""), MessageListModel::class.java)
}