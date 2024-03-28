package ru.kpfu.itis.paramonov.question_api.data.repository

import ru.kpfu.itis.paramonov.question_api.data.mapper.QuestionDomainModelMapper
import ru.kpfu.itis.paramonov.question_api.data.remote.QuestionApi
import ru.kpfu.itis.paramonov.question_api.domain.model.QuestionDomainModel
import ru.kpfu.itis.paramonov.question_api.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val api: QuestionApi,
    private val mapper: QuestionDomainModelMapper
): QuestionRepository {
    override suspend fun getQuestions(amount: Int, difficulty: String, category: Int): QuestionDomainModel {
        val result = api.getQuestions(amount, difficulty, category)
        return mapper.map(result)
    }
}