package ru.kpfu.itis.paramonov.feature_questions.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.QuestionSettingsRepository

interface FeatureQuestionsDependencies {

    fun sharedPreferencesRepository(): QuestionSettingsRepository

    fun dispatcher(): CoroutineDispatcher

    fun resourceManager(): ResourceManager
}