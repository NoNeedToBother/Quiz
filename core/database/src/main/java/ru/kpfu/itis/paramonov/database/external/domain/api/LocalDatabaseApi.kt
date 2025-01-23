package ru.kpfu.itis.paramonov.database.external.domain.api

import ru.kpfu.itis.paramonov.database.external.domain.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.database.external.domain.repository.SavedQuestionRepository

interface LocalDatabaseApi {

    fun sharedPreferencesRepository(): QuestionSettingsRepository

    fun savedQuestionsRepository(): SavedQuestionRepository
}