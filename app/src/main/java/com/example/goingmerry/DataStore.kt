package com.example.goingmerry

import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class DataStore(
    private val sharedPrefs: SharedPreferences,
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val EXPIRED_TOKEN = longPreferencesKey("expired_token")
        private val TOKEN = stringPreferencesKey("TOKEN")
    }
    suspend fun saveToken(token: String, expired: Long): Int {
        var value = 1
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[EXPIRED_TOKEN] = expired
            runBlocking {
                value = 2
            }
        }
        return value
    }
    suspend fun readExpired(): Long{
        val value: Flow<Long> = dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[EXPIRED_TOKEN] ?: 0L
            }
        return value .first()
    }

    suspend fun readToken(): String{
        val exampleCounterFlow: Flow<String> = dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[TOKEN] ?: ""
            }
        return exampleCounterFlow.first()
    }

    suspend fun clear(): Unit{
    }
}