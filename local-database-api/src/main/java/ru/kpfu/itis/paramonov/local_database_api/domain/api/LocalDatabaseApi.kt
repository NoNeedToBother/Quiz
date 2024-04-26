package ru.kpfu.itis.paramonov.local_database_api.domain.api

import ru.kpfu.itis.paramonov.local_database_api.domain.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.SavedQuestionRepository

interface LocalDatabaseApi {

    fun sharedPreferencesRepository(): QuestionSettingsRepository

    fun savedQuestionsRepository(): SavedQuestionRepository
}