package ru.kpfu.itis.paramonov.leaderboards.api.usecase

import ru.kpfu.itis.paramonov.core.model.data.GameMode

interface GetGameModeUseCase {

    suspend operator fun invoke(): GameMode
}
