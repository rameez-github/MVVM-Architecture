package com.mvvm.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserStore @Inject constructor (val context: Context) {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore("MVVMExample")
        private val STAFF_ID = stringPreferencesKey("staff_user_id")
    }

    val getStaffId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[STAFF_ID]
    }


    suspend fun saveStaffId(staffId: String?) {
        context.dataStore.edit { preferences ->
            preferences[STAFF_ID] = staffId?: ""
        }
    }


    suspend fun clearData() {
        context.dataStore.edit { it.clear() }
    }
}