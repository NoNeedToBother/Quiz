package ru.kpfu.itis.paramonov.feature_questions.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.utils.HtmlDecoder
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.ResultRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository
import ru.kpfu.itis.paramonov.database.external.domain.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.database.external.domain.repository.SavedQuestionRepository
import ru.kpfu.itis.paramonov.network.external.domain.repository.QuestionRepository

interface FeatureQuestionsDependencies {

    fun questionSettingsRepository(): QuestionSettingsRepository

    fun savedQuestionsRepository(): SavedQuestionRepository

    fun dispatcher(): CoroutineDispatcher

    fun resourceManager(): ResourceManager

    fun questionRepository(): QuestionRepository

    fun htmlDecoder(): HtmlDecoder

    fun resultRepository(): ResultRepository

    fun userRepository(): UserRepository
}