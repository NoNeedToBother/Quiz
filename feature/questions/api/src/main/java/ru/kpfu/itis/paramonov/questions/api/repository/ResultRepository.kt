package ru.kpfu.itis.paramonov.questions.api.repository

import ru.kpfu.itis.paramonov.questions.api.model.Result

interface ResultRepository {

    suspend fun getMaxScore(): Double

    suspend fun save(result: Result): Double
}
