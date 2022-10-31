package com.arya.storyapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "Session.ds")

class SessionDataStore(private val context: Context) {

    fun retrieveSession(): LiveData<SessionModel> {
        return context.dataStore.data.map { preferences ->
            SessionModel(
                preferences[NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }.distinctUntilChanged().asLiveData()
    }

    suspend fun saveSession(sessionModel: SessionModel) {
        context.dataStore.edit { preferences ->
            preferences[NAME_KEY] = sessionModel.name
            preferences[TOKEN_KEY] = "Bearer ${sessionModel.token}"
            preferences[STATE_KEY] = sessionModel.isLogin
        }
    }

    suspend fun deleteSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")
    }
}