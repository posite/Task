package com.posite.task.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.util.Date
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_info_data_store")

class DataStoreUtil @Inject constructor(private val context: Context) {

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    suspend fun loadUserName(): String {
        val preferences = context.dataStore.data.first()
        return preferences[USER_NAME_KEY] ?: return ""
    }

    suspend fun saveUserProfile(profile: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_PROFILE_KEY] = profile
        }
    }

    suspend fun loadUserProfile(): String {
        val preferences = context.dataStore.data.first()
        return preferences[USER_PROFILE_KEY] ?: return ""
    }

    suspend fun saveUserBirthday(birthday: Date) {
        context.dataStore.edit { preferences ->
            preferences[USER_BIRTHDAY_KEY] = birthday.time.toString()
        }
    }

    suspend fun loadUserBirthday(): Date? {
        val preferences = context.dataStore.data.first()
        return if (preferences[USER_BIRTHDAY_KEY] != null) {
            Date(preferences[USER_BIRTHDAY_KEY]!!.toLong())
        } else {
            null
        }
    }

    companion object {
        private val USER_BIRTHDAY_KEY = stringPreferencesKey("user_birthday")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_PROFILE_KEY = stringPreferencesKey("user_profile")
    }

    private fun ByteArray.toBase64(): String =
        android.util.Base64.encodeToString(this, android.util.Base64.NO_WRAP)

    private fun String.fromBase64(): ByteArray =
        android.util.Base64.decode(this, android.util.Base64.NO_WRAP)
}