package ru.kpfu.itis.paramonov.feature_leaderboards.domain.mapper

import ru.kpfu.itis.paramonov.common.mapper.ModelMapper
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.firebase.domain.model.FirebaseUser
import ru.kpfu.itis.paramonov.firebase.domain.model.Result
import javax.inject.Inject

class ResultUiModelMapper @Inject constructor(
    private val questionSettingsUiModelMapper: QuestionSettingsUiModelMapper
): ModelMapper<Result, ResultUiModel> {
    override fun map(model: Result): ResultUiModel {
        return ResultUiModel(
            user = mapUser(model.user),
            time = model.time, correct = model.correct,
            total = model.total, score = model.score,
            difficulty = questionSettingsUiModelMapper.mapDifficulty(model.difficulty),
            category = questionSettingsUiModelMapper.mapCategory(model.category),
            gameMode = questionSettingsUiModelMapper.mapGameMode(model.gameMode)
        )
    }

    private fun mapUser(model: FirebaseUser): UserModel {
        return UserModel(
            id = model.id,
            username = model.username,
            profilePictureUrl = model.profilePictureUrl,
            dateRegistered = model.dateRegistered,
            info = model.info
        )
    }
}