package com.posite.task.util

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_info_data_store")

class DataStoreUtil @Inject constructor(private val context: Context) {

    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            Log.d("name save", name)
            preferences[USER_NAME_KEY] = name
        }
    }

    suspend fun loadUserName(): String {
        val preferences = context.dataStore.data.first()
        Log.d("name load", preferences[USER_NAME_KEY].toString())
        return preferences[USER_NAME_KEY] ?: return ""
    }

    suspend fun saveUserProfile(profile: String) {
        context.dataStore.edit { preferences ->
            Log.d("profile save", profile)
            preferences[USER_PROFILE_KEY] = profile
        }
    }

    suspend fun loadUserProfile(): String {
        val preferences = context.dataStore.data.first()
        Log.d("profile load", preferences[USER_PROFILE_KEY].toString())
        return preferences[USER_PROFILE_KEY] ?: return ""
    }

    suspend fun saveUserBirthday(birthday: String) {
        Log.d("birthday save", birthday)
        context.dataStore.edit { preferences ->
            preferences[USER_BIRTHDAY_KEY] = birthday
        }
    }

    suspend fun loadUserBirthday(): String {
        val preferences = context.dataStore.data.first()
        Log.d("birthday load", preferences[USER_BIRTHDAY_KEY].toString())
        return preferences[USER_BIRTHDAY_KEY] ?: return ""
    }

    companion object {
        private val USER_BIRTHDAY_KEY = stringPreferencesKey("user_birthday")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_PROFILE_KEY = stringPreferencesKey("user_profile")
    }
}