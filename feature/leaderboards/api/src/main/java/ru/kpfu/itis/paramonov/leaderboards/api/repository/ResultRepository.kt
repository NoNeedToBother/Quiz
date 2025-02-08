package ru.kpfu.itis.paramonov.leaderboards.api.repository

import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.leaderboards.api.model.Result

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

}
