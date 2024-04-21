package ru.kpfu.itis.paramonov.question_api.domain.api

import ru.kpfu.itis.paramonov.question_api.domain.repository.QuestionRepository

interface QuestionApi {

    fun questionRepository(): QuestionRepository
}