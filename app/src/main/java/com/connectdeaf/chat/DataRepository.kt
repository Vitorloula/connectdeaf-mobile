package com.connectdeaf.chat

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataRepository(private val context: Context) {
    private companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("dataRepository")

        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
    }

    suspend fun getCurrentUserEmail(): String {
        return context.dataStore.data.map { preferences ->
            preferences[USER_EMAIL] ?: "Unknown"
        }.first()
    }

    suspend fun saveUserEmail(userEmail: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = userEmail
        }
    }

    suspend fun getCurrentUserId(): String {
        return context.dataStore.data.map { preferences ->
            preferences[USER_ID] ?: "Unknown"
        }.first()
    }

    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun getCurrentUserName(): String {
        return context.dataStore.data.map { preferences ->
            preferences[USER_NAME] ?: "Unknown"
        }.first()
    }

    suspend fun saveUserName(userName: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
        }
    }
}