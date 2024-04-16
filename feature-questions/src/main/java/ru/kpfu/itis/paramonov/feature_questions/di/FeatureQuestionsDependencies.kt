package ru.kpfu.itis.paramonov.feature_questions.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.SharedPreferencesRepository

interface FeatureQuestionsDependencies {

    fun sharedPreferencesRepository(): SharedPreferencesRepository

    fun dispatcher(): CoroutineDispatcher
}