package ru.kpfu.itis.paramonov.feature_profiles.presentation.model

import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.common.utils.DateTime

data class ResultUiModel(
    val id: String,
    val user: UserModel,
    val score: Double,
    val date: DateTime
)