package ru.kpfu.itis.paramonov.local_database_api.domain.api

import ru.kpfu.itis.paramonov.local_database_api.domain.repository.QuestionSettingsRepository

interface LocalDatabaseApi {

    fun sharedPreferencesRepository(): QuestionSettingsRepository
}