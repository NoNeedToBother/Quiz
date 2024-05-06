package ru.kpfu.itis.paramonov.question_api.external.domain.api

import ru.kpfu.itis.paramonov.question_api.external.domain.repository.QuestionRepository

interface QuestionApi {

    fun questionRepository(): QuestionRepository
}