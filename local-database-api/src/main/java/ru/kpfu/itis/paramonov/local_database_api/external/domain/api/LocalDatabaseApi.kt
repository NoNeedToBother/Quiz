package ru.kpfu.itis.paramonov.local_database_api.external.domain.api

import ru.kpfu.itis.paramonov.local_database_api.external.domain.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.local_database_api.external.domain.repository.SavedQuestionRepository

interface LocalDatabaseApi {

    fun sharedPreferencesRepository(): QuestionSettingsRepository

    fun savedQuestionsRepository(): SavedQuestionRepository
}