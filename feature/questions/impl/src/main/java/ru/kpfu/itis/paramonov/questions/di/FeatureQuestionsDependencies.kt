package ru.kpfu.itis.paramonov.questions.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.utils.HtmlDecoder
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionRepository
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.questions.api.repository.SavedQuestionRepository
import ru.kpfu.itis.paramonov.questions.api.repository.UserRepository

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