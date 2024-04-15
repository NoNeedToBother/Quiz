package ru.kpfu.itis.paramonov.local_database_api.domain.api

import android.content.SharedPreferences

interface LocalDatabaseApi {

    fun sharedPreferencesRepository(): SharedPreferences
}