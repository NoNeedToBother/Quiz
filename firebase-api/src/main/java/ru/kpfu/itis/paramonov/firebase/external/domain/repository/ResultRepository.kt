package ru.kpfu.itis.paramonov.firebase.external.domain.repository

import ru.kpfu.itis.paramonov.common.model.data.Category
import ru.kpfu.itis.paramonov.common.model.data.Difficulty
import ru.kpfu.itis.paramonov.common.model.data.GameMode
import ru.kpfu.itis.paramonov.firebase.external.domain.model.Result

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
}