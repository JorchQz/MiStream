package com.mistream.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mistream.app.di.RealDebridTokenHolder
import com.mistream.app.di.TmdbTokenHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val KEY_RD_TOKEN = stringPreferencesKey("rd_token")
        val KEY_TMDB_KEY = stringPreferencesKey("tmdb_key")
    }

    val rdToken: Flow<String> = dataStore.data.map { it[KEY_RD_TOKEN] ?: "" }
    val tmdbKey: Flow<String> = dataStore.data.map { it[KEY_TMDB_KEY] ?: TmdbTokenHolder.apiKey }

    suspend fun saveRdToken(token: String) {
        dataStore.edit { it[KEY_RD_TOKEN] = token }
        RealDebridTokenHolder.token = token
    }

    suspend fun saveTmdbKey(key: String) {
        dataStore.edit { it[KEY_TMDB_KEY] = key }
        TmdbTokenHolder.apiKey = key
    }

    suspend fun loadTokens() {
        val prefs = dataStore.data.first()
        prefs[KEY_RD_TOKEN]?.takeIf { it.isNotEmpty() }?.let { RealDebridTokenHolder.token = it }
        prefs[KEY_TMDB_KEY]?.takeIf { it.isNotEmpty() }?.let { TmdbTokenHolder.apiKey = it }
    }

    suspend fun getRdToken(): String = dataStore.data.first()[KEY_RD_TOKEN] ?: ""
    suspend fun getTmdbKey(): String = dataStore.data.first()[KEY_TMDB_KEY] ?: TmdbTokenHolder.apiKey
}
