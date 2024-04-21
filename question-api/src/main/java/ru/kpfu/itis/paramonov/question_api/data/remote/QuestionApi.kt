package ru.kpfu.itis.paramonov.question_api.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kpfu.itis.paramonov.question_api.data.model.CategoryResponse
import ru.kpfu.itis.paramonov.question_api.data.model.QuestionResponse

interface QuestionApi {

    @GET("/api.php")
    suspend fun getQuestions(
        @Query(value = "amount") amount: Int,
        @Query(value = "difficulty") difficulty: String,
        @Query(value = "category") category: Int
    ): QuestionResponse

    @GET("/api_category.php")
    suspend fun getCategoriesId(): CategoryResponse

}