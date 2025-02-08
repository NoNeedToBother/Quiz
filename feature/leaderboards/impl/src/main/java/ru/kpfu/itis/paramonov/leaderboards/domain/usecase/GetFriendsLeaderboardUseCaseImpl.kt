package ru.kpfu.itis.paramonov.leaderboards.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.leaderboards.api.model.Result
import ru.kpfu.itis.paramonov.leaderboards.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.leaderboards.api.usecase.GetFriendsLeaderboardUseCase

class GetFriendsLeaderboardUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val resultRepository: ResultRepository
): GetFriendsLeaderboardUseCase {

    override suspend operator fun invoke(
        gameMode: GameMode,
        difficulty: Difficulty?,
        category: Category?,
        max: Int, afterScore: Double?
    ): List<Result> {
        return withContext(dispatcher) {
            val results = mutableListOf<Result>()
            resultRepository.getFriendsResults(
                gameMode = gameMode,
                difficulty = difficulty,
                category = category,
                max = max, afterScore = afterScore
            ).forEach { result ->
                result.id.let {
                    if (it.isNotEmpty()) results.add(result)
                }
            }
            results
        }
    }
}
