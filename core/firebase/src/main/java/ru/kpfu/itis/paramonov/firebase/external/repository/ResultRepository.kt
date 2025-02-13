package ru.kpfu.itis.paramonov.firebase.external.repository

import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.firebase.external.model.Result

interface ResultRepository {

    suspend fun getGlobalResults(gameMode: GameMode,
                                 difficulty: Difficulty?,
                                 category: Category?,
                                 max: Int, afterScore: Double?
    ): List<Result>

    suspend fun getFriendsResults(gameMode: GameMode,
                                  difficulty: Difficulty?,
                                  category: Category?,
                                  max: Int, afterScore: Double?
    ): List<Result>

    suspend fun save(result: Result): Double

    suspend fun getMaxScore(): Double

    suspend fun getLastResults(max: Int): List<Result>

    suspend fun getLastResults(max: Int, id: String): List<Result>
}
