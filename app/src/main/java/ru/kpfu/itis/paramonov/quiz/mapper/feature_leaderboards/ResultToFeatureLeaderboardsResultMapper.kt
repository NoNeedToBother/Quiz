package ru.kpfu.itis.paramonov.quiz.mapper.feature_leaderboards

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.firebase.external.domain.model.Result
import javax.inject.Inject

class ResultToFeatureLeaderboardsResultMapper @Inject constructor(
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