package ru.kpfu.itis.paramonov.questions.api.api

import ru.kpfu.itis.paramonov.questions.api.repository.QuestionRepository
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.questions.api.repository.SavedQuestionRepository
import ru.kpfu.itis.paramonov.questions.api.repository.UserRepository

interface FeatureQuestionsApi {

    fun userRepository(): UserRepository

    fun questionRepository(): QuestionRepository

    fun questionSettingsRepository(): QuestionSettingsRepository

    fun resultRepository(): ResultRepository

    fun savedQuestionRepository(): SavedQuestionRepository

}