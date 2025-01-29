package ru.kpfu.itis.paramonov.leaderboards.domain.mapper

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.leaderboards.api.model.Result
import javax.inject.Inject

class ResultUiModelMapper @Inject constructor(
    private val questionSettingsUiModelMapper: QuestionSettingsUiModelMapper,
    private val userUiModelMapper: UserUiModelMapper,
): ModelMapper<Result, ResultUiModel> {
    override fun map(model: Result): ResultUiModel {
        return ResultUiModel(
            id = model.id,
            user = userUiModelMapper.map(model.user),
            time = model.time, correct = model.correct,
            total = model.total, score = model.score,
            difficulty = questionSettingsUiModelMapper.mapDifficulty(model.difficulty),
            category = questionSettingsUiModelMapper.mapCategory(model.category),
            gameMode = questionSettingsUiModelMapper.mapGameMode(model.gameMode)
        )
    }
}