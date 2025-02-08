package ru.kpfu.itis.paramonov.quiz.mapper.profiles

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.profiles.api.model.Result

class ResultToFeatureProfilesResultMapper(
    private val firebaseUserToFeatureProfilesUserMapper: FirebaseUserToFeatureProfilesUserMapper
): ModelMapper<ru.kpfu.itis.paramonov.firebase.external.domain.model.Result, Result> {
    override fun map(model: ru.kpfu.itis.paramonov.firebase.external.domain.model.Result): Result {
        return Result(
            id = model.id ?: "",
            user = firebaseUserToFeatureProfilesUserMapper.map(model.user),
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
