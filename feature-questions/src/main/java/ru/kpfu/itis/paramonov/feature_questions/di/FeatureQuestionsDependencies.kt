package ru.kpfu.itis.paramonov.feature_questions.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.HtmlDecoder
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.question_api.domain.repository.QuestionRepository

interface FeatureQuestionsDependencies {

    fun questionSettingsRepository(): QuestionSettingsRepository

    fun dispatcher(): CoroutineDispatcher

    fun resourceManager(): ResourceManager

    fun questionRepository(): QuestionRepository

    fun htmlDecoder(): HtmlDecoder
}