package ru.kpfu.itis.paramonov.profiles.api.repository

import ru.kpfu.itis.paramonov.profiles.api.model.Result

interface ResultRepository {

    suspend fun getLastResults(max: Int): List<Result>

    suspend fun getLastResults(max: Int, id: String): List<Result>

}
