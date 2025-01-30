package ru.kpfu.itis.paramonov.questions.di

import ru.kpfu.itis.paramonov.core.di.FeatureContainer
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionRepository
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.questions.api.repository.SavedQuestionRepository
import ru.kpfu.itis.paramonov.questions.api.repository.UserRepository

interface FeatureQuestionsDependenciesContainer: FeatureContainer {
    fun userRepository(): UserRepository

    fun questionRepository(): QuestionRepository

    fun questionSettingsRepository(): QuestionSettingsRepository

    fun resultRepository(): ResultRepository

    fun savedQuestionRepository(): SavedQuestionRepository
}