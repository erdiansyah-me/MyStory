package com.erdiansyah.mystory.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.erdiansyah.mystory.data.remote.LoginResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "UserPreference")

class UserPreference @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.dataStore

    fun getUser(): Flow<LoginResult?> {
        return dataStore.data.map { preferences ->
            if (preferences[TOKEN_KEY] != null) {
                LoginResult (
                    preferences[NAME_KEY] ?:"",
                    preferences[USERID_KEY] ?:"",
                    preferences[TOKEN_KEY] ?:""
                )
            } else {
                null
            }
        }
    }

    suspend fun saveUser(user: LoginResult) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[USERID_KEY] = user.userId
            preferences[TOKEN_KEY] = user.token
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(NAME_KEY)
            preferences.remove(USERID_KEY)
            preferences.remove(TOKEN_KEY)
        }
    }
    companion object {
        private val NAME_KEY = stringPreferencesKey("name")
        private val USERID_KEY = stringPreferencesKey("userId")
        private val TOKEN_KEY = stringPreferencesKey("token")
    }
}