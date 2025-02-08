package ru.kpfu.itis.paramonov.quiz.mapper.leaderboards

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.firebase.external.model.Result

class ResultToFeatureLeaderboardsResultMapper(
    private val firebaseUserToFeatureLeaderboardsUserMapper: FirebaseUserToFeatureLeaderboardsUserMapper
): ModelMapper<Result, ru.kpfu.itis.paramonov.leaderboards.api.model.Result> {
    override fun map(model: Result): ru.kpfu.itis.paramonov.leaderboards.api.model.Result {
        return ru.kpfu.itis.paramonov.leaderboards.api.model.Result(
            id = model.id ?: "",
            user = firebaseUserToFeatureLeaderboardsUserMapper.map(model.user),
            time = model.time,
            correct = model.correct,
            total = model.total,
            difficulty = model.difficulty,
            category = model.category,
            gameMode = model.gameMode,
            score = model.score,
            date = model.date
        )
    }
}
