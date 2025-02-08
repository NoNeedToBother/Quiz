package ru.kpfu.itis.paramonov.leaderboards.api.usecase

import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.leaderboards.api.model.Result

interface GetGlobalLeaderboardUseCase {

    suspend operator fun invoke(
        gameMode: GameMode,
        difficulty: Difficulty?,
        category: Category?,
        max: Int, afterScore: Double?
    ): List<Result>

}
