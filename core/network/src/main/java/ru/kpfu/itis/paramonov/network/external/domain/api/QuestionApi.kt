package ru.kpfu.itis.paramonov.network.external.domain.api

import ru.kpfu.itis.paramonov.network.external.domain.repository.QuestionRepository

interface QuestionApi {

    fun questionRepository(): QuestionRepository
}