package ru.kpfu.itis.paramonov.profiles.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.profiles.api.model.Result
import ru.kpfu.itis.paramonov.profiles.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetUserLastResultsUseCase
import javax.inject.Inject

class GetUserLastResultsUseCaseImpl @Inject constructor(
    private val resultRepository: ResultRepository,
    private val dispatcher: CoroutineDispatcher
): GetUserLastResultsUseCase {

    override suspend operator fun invoke(max: Int, id: String): List<Result> {
        return withContext(dispatcher) {
            resultRepository.getLastResults(max, id)
        }
    }
}