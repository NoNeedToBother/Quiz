package ru.kpfu.itis.paramonov.feature_profiles.domain.mapper

import ru.kpfu.itis.paramonov.common.mapper.ModelMapper
import ru.kpfu.itis.paramonov.feature_profiles.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.firebase.external.domain.model.Result
import javax.inject.Inject

class ResultUiModelMapper @Inject constructor(
    private val userUiModelMapper: UserUiModelMapper,
): ModelMapper<Result, ResultUiModel> {
    override fun map(model: Result): ResultUiModel {
        return ResultUiModel(
            id = model.id ?: "",
            user = userUiModelMapper.map(model.user),
            score = model.score,
            date = model.date
        )
    }
}