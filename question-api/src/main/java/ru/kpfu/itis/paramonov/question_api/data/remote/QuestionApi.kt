package ru.kpfu.itis.paramonov.question_api.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kpfu.itis.paramonov.question_api.data.model.QuestionResponse

interface QuestionApi {

    @GET
    suspend fun getQuestions(
        @Query(value = "amount") amount: Int,
        @Query(value = "difficulty") difficulty: String,
        @Query(value = "category") category: Int
    ): QuestionResponse

}