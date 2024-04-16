package ru.kpfu.itis.paramonov.local_database_api.domain.api

import ru.kpfu.itis.paramonov.local_database_api.domain.repository.SharedPreferencesRepository

interface LocalDatabaseApi {

    fun sharedPreferencesRepository(): SharedPreferencesRepository
}