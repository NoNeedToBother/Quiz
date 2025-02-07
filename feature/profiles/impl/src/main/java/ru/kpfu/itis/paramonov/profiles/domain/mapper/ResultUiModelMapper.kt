package ru.kpfu.itis.paramonov.profiles.domain.mapper

import ru.kpfu.itis.paramonov.core.mapper.ModelMapper
import ru.kpfu.itis.paramonov.profiles.api.model.Result
import ru.kpfu.itis.paramonov.profiles.presentation.model.ResultUiModel

class ResultUiModelMapper(
    private val userUiModelMapper: UserUiModelMapper,
): ModelMapper<Result, ResultUiModel> {
    override fun map(model: Result): ResultUiModel {
        return ResultUiModel(
            id = model.id,
            user = userUiModelMapper.map(model.user),
            score = model.score,
            date = model.date
        )
    }
}