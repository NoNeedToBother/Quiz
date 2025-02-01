package ru.kpfu.itis.paramonov.profiles.api.usecase

import ru.kpfu.itis.paramonov.profiles.api.model.Result

interface GetUserLastResultsUseCase {

    suspend operator fun invoke(max: Int, id: String): List<Result>

}