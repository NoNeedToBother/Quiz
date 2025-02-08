package ru.kpfu.itis.paramonov.profiles.presentation.model

import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.core.utils.DateTime

data class ResultUiModel(
    val id: String,
    val user: UserModel,
    val score: Double,
    val date: DateTime
)
