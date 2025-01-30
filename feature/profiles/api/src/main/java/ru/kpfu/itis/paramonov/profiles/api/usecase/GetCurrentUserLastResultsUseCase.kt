package ru.kpfu.itis.paramonov.profiles.api.usecase

import ru.kpfu.itis.paramonov.profiles.api.model.Result

interface GetCurrentUserLastResultsUseCase {

    suspend operator fun invoke(max: Int): List<Result>
}