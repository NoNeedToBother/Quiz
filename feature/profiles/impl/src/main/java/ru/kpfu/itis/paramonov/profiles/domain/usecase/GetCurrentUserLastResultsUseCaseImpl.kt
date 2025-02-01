package ru.kpfu.itis.paramonov.profiles.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.profiles.api.model.Result
import ru.kpfu.itis.paramonov.profiles.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetCurrentUserLastResultsUseCase
import javax.inject.Inject

class GetCurrentUserLastResultsUseCaseImpl @Inject constructor(
    private val resultRepository: ResultRepository,
    private val dispatcher: CoroutineDispatcher
): GetCurrentUserLastResultsUseCase {

    override suspend operator fun invoke(max: Int): List<Result> {
        return withContext(dispatcher) {
            resultRepository.getLastResults(max)
        }
    }
}